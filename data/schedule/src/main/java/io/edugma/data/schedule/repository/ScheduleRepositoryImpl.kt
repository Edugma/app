package io.edugma.data.schedule.repository

import io.edugma.data.base.consts.CacheConst
import io.edugma.data.base.consts.PrefConst
import io.edugma.data.base.local.*
import io.edugma.data.base.model.map
import io.edugma.data.base.store.StoreImpl
import io.edugma.data.schedule.api.FreePlacesService
import io.edugma.data.schedule.api.ScheduleInfoService
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.data.schedule.api.ScheduleSourcesService
import io.edugma.data.schedule.model.ScheduleDao
import io.edugma.domain.base.utils.Lce
import io.edugma.domain.base.utils.map
import io.edugma.domain.schedule.model.compact.CompactLessonFeatures
import io.edugma.domain.schedule.model.compact.CompactSchedule
import io.edugma.domain.schedule.model.compact.ScheduleInfo
import io.edugma.domain.schedule.model.group.Group
import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonDateTime
import io.edugma.domain.schedule.model.lesson.LessonDateTimes
import io.edugma.domain.schedule.model.lesson.LessonTime
import io.edugma.domain.schedule.model.place.Place
import io.edugma.domain.schedule.model.place.PlaceFilters
import io.edugma.domain.schedule.model.schedule.LessonsByTime
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import io.edugma.domain.schedule.model.source.ScheduleSources
import io.edugma.domain.schedule.model.teacher.Teacher
import io.edugma.domain.schedule.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.util.*
import kotlin.time.Duration.Companion.days

class ScheduleRepositoryImpl(
    private val scheduleService: ScheduleService,
    private val scheduleSourcesService: ScheduleSourcesService,
    private val cachedDS: CacheVersionLocalDS,
    private val preferencesDS: PreferencesDS
) : ScheduleRepository {
    override fun getSourceTypes() =
        scheduleSourcesService.getSourceTypes()
            .flowOn(Dispatchers.IO)

    override fun getRawSchedule(source: ScheduleSource): Flow<Lce<CompactSchedule?>> {
        return scheduleStore.get(source, false)
    }

    override fun getSources(type: ScheduleSources) =
        scheduleSourcesService.getSources(type.name.lowercase())
            .flowOn(Dispatchers.IO)

    override suspend fun setSelectedSource(source: ScheduleSourceFull) {
        preferencesDS.set(source, PrefConst.SelectedScheduleSource)
    }

    override fun getSelectedSource() = preferencesDS
        .flowOf<ScheduleSourceFull>(PrefConst.SelectedScheduleSource)
        .flowOn(Dispatchers.IO)


    private val scheduleStore = StoreImpl<ScheduleSource, CompactSchedule>(
        fetcher = { key -> scheduleService.getCompactSchedule(key.type.name.lowercase(), key.key) },
        reader = { key ->
            cachedDS.getFlow<ScheduleDao>(CacheConst.Schedule + key, expireAt)
                .map { it.map { it?.days } }
        },
        writer = { key, data ->
            flowOf(cachedDS.save(ScheduleDao.from(key, data), CacheConst.Schedule + key))
        },
        expireAt = 1.days
    )

    override fun getSchedule(source: ScheduleSource, forceUpdate: Boolean) =
        scheduleStore.get(source, forceUpdate)
            .map { it.map { it?.toModel() ?: emptyList() } }
            .flowOn(Dispatchers.IO)

    override fun getLessonsReview(source: ScheduleSource) =
        scheduleService.getLessonsReview(source.type.name.lowercase(), source.key)
            .flowOn(Dispatchers.IO)
}

fun CompactLessonFeatures.toModel(info: ScheduleInfo): Lesson {
    return Lesson(
        title = info.subjectsInfo.first { it.id == subjectId }.title,
        type = info.typesInfo.first { it.id == typeId }.title,
        teachers = teachersId.map { id ->
            val temp = info.teachersInfo.first { it.id == id }
            Teacher(
                id = temp.id,
                name = temp.name
            )
        },
        groups = groupsId.map { id ->
            val temp = info.groupsInfo.first { it.id == id }
            Group(
                id = temp.id,
                title = temp.title
            )
        },
        places = placesId.map { id ->
            val temp = info.placesInfo.first { it.id == id }
            Place(
                id = temp.id,
                title = temp.title,
                description = temp.title
            )
        },
    )
}

fun CompactSchedule.toModel(): List<ScheduleDay> {
    val lessons: List<LessonDateTimes> = this.lessons.map {
        LessonDateTimes(
            lesson = it.lesson.toModel(info),
            time = it.times
        )
    }
    val (dateFrom, dateTo) = getLessonDateRange(lessons)

    return buildSchedule(
        lessons = lessons,
        dateFrom = dateFrom,
        dateTo = dateTo
    )
}

private fun buildSchedule(
    lessons: List<LessonDateTimes>,
    dateFrom: LocalDate,
    dateTo: LocalDate
): List<ScheduleDay> {
    val resMap: MutableMap<LocalDate, MutableMap<LessonTime, MutableList<Lesson>>> = TreeMap()

    var currentDay = dateFrom
    do {
        resMap[currentDay] = TreeMap<LessonTime, MutableList<Lesson>>()
        currentDay = currentDay.plusDays(1)
    } while (currentDay <= dateTo)

    for (lessonDateTimes in lessons) {
        for (dateTime in lessonDateTimes.time.getLessonDates()) {
            val timeToLessonsMap = resMap.getOrPut(dateTime.date) { TreeMap<LessonTime, MutableList<Lesson>>() }
            val lessonList = timeToLessonsMap.getOrPut(dateTime.time) { mutableListOf() }
            lessonList.add(lessonDateTimes.lesson)
        }
    }

    val lessons = resMap.map { (key, value) ->
        val l1 = value.map { (key2, value2) ->
            LessonsByTime(
                time = key2,
                value2
            )
        }

        ScheduleDay(
            date = key,
            lessons = l1
        )
    }

    return lessons
}

data class LocalLessonDateTime(
    val date: LocalDate,
    val time: LessonTime
)

private fun List<LessonDateTime>.getLessonDates(): List<LocalLessonDateTime> {
    return asSequence()
        .flatMap { it.toDates() }
        .toList()
}

fun LessonDateTime.toDates(): Sequence<LocalLessonDateTime> {
    return sequence {
        if (endDate == null) {
            yield(LocalLessonDateTime(startDate, time))
        } else {
            yieldAll(generateDatesFromRange(startDate, endDate!!, time))
        }
    }
}

private fun generateDatesFromRange(startDate: LocalDate, endDate: LocalDate, time: LessonTime) =
    sequence {
        var currentDay = startDate
        do {
            yield(LocalLessonDateTime(currentDay, time))
            currentDay = currentDay.plusDays(7)
        } while (currentDay <= endDate)
    }

private fun getLessonDateRange(lessons: List<LessonDateTimes>): Pair<LocalDate, LocalDate> {
    var minDate = LocalDate.MAX
    var maxDate = LocalDate.MIN

    for (lessonDateTimes in lessons) {
        for (dateTime in lessonDateTimes.time) {
            if (dateTime.startDate < minDate) {
                minDate = dateTime.startDate
            }

            if (dateTime.endDate == null) {
                if (dateTime.startDate > maxDate) {
                    maxDate = dateTime.startDate
                }
            } else {
                if (dateTime.endDate!! > maxDate) {
                    maxDate = dateTime.endDate!!
                }
            }
        }
    }

    if (minDate == LocalDate.MAX && maxDate == LocalDate.MIN) {
        minDate = LocalDate.now()
        maxDate = LocalDate.now()
    }

    return minDate to maxDate
}

private fun getDateRange(lessons: List<LocalDate>): Pair<LocalDate, LocalDate> {
    var minDate = LocalDate.MAX
    var maxDate = LocalDate.MIN

    for (dateTime in lessons) {
        if (dateTime < minDate) {
            minDate = dateTime
        }

        if (dateTime > maxDate) {
            maxDate = dateTime
        }
    }

    return minDate to maxDate
}