package io.edugma.features.schedule.domain.usecase

import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import io.edugma.features.schedule.domain.model.lessonSubject.LessonSubject
import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.repository.ScheduleRepository
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.math.max

class ScheduleHistoryUseCase(
    private val repository: ScheduleRepository,
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
    private val scheduleRepository: ScheduleRepository,
) {
    fun getHistory() =
        scheduleSourcesRepository.getSelectedSource()
            .transformLatest { source ->
                if (source == null) {
                    error("")
                } else {
                    emitAll(repository.getHistory(ScheduleSource(source.type, source.id)))
                }
            }.map { it.asReversed() }

    suspend fun getChanges(
        firstScheduleTimestamp: Instant,
        secondScheduleTimestamp: Instant,
    ): List<ScheduleDayChange> {
        val source = scheduleSourcesRepository.getSelectedSource().first() ?: return emptyList()
        val scheduleSource = ScheduleSource(source.type, source.id)
        val firstSchedule = scheduleRepository.getHistoryRecord(
            source = scheduleSource,
            timestamp = firstScheduleTimestamp,
        ) ?: return emptyList()
        val secondSchedule = scheduleRepository.getHistoryRecord(
            source = scheduleSource,
            timestamp = secondScheduleTimestamp,
        ) ?: return emptyList()

        return calculateChanges(firstSchedule.schedule, secondSchedule.schedule)
    }

    private fun calculateChanges(
        oldSchedule: List<ScheduleDay>,
        newSchedule: List<ScheduleDay>,
    ): List<ScheduleDayChange> {
        val dateToLessons = mutableMapOf<LocalDate, MutableList<List<LessonEvent>>>()

        oldSchedule.forEach {
            val list = dateToLessons.getOrPut(it.date) { ArrayList(2) }
            list.add(it.lessons)
        }

        newSchedule.forEach {
            val list = dateToLessons.getOrPut(it.date) { ArrayList(2) }
            list.add(it.lessons)
        }

        val lessons = dateToLessons.map { (date, pair) ->
            val oldScheduleDay = pair.getOrElse(0) { emptyList() }
            val newScheduleDay = pair.getOrElse(1) { emptyList() }

            val changes = getDayChanges(
                oldDay = oldScheduleDay,
                newDay = newScheduleDay,
            )

            ScheduleDayChange(
                date = date,
                lessons = changes,
            )
        }.filter { it.lessons.isNotEmpty() }
            .sortedBy { it.date }

        return lessons
    }

    // TODO LessonsByTime -> LessonEvent
    private fun getDayChanges(
        oldDay: List<LessonEvent>,
        newDay: List<LessonEvent>,
    ): List<LessonsByTimeChange> {
        val timeToLessons = mutableMapOf<LessonTime, MutableList<List<LessonEvent>>>()

//        oldDay.forEach {
//            val list = timeToLessons.getOrPut(it.) { ArrayList(2) }
//            list.add(it)
//        }
//
//        newDay.forEach {
//            val list = timeToLessons.getOrPut(it.time) { ArrayList(2) }
//            list.add(it.lessons)
//        }

        val lessons = timeToLessons.map { (time, pair) ->
            val old = pair.getOrElse(0) { emptyList() }
            val new = pair.getOrElse(1) { emptyList() }

            LessonsByTimeChange(
                time = time,
                lessons = getPlaceChanges(old, new),
            )
        }.filter { it.lessons.isNotEmpty() }
            .sortedBy { it.time }

        return lessons
    }

    private fun getPlaceChanges(
        oldLessonsByTime: List<LessonEvent>,
        newLessonsByTime: List<LessonEvent>,
    ): List<LessonChange> {
        val groupedBySubject = mutableMapOf<LessonSubject, MutableList<MutableList<LessonEvent>>>()

        oldLessonsByTime.forEach { lesson ->
            val list = groupedBySubject.getOrPut(lesson.subject) { MutableList(2) { mutableListOf() } }
            list[0].add(lesson)
        }

        newLessonsByTime.forEach { lesson ->
            val list = groupedBySubject.getOrPut(lesson.subject) { MutableList(2) { mutableListOf() } }
            // Skip lesson if it in list[0] and remove it from list[0]
            if (list[0].remove(lesson).not()) {
                list[1].add(lesson)
            }
        }

        val lessons = groupedBySubject.map { (subject, pair) ->
            val oldList = pair.getOrElse(0) { emptyList() }
            val newList = pair.getOrElse(1) { emptyList() }

            val size = max(oldList.size, newList.size)

            (0 until size).mapNotNull { index ->
                getLessonChanges(
                    oldLesson = oldList.getOrNull(index),
                    newLesson = newList.getOrNull(index),
                )
            }
        }

        return lessons.flatten()
    }

    private fun getLessonChanges(
        oldLesson: LessonEvent?,
        newLesson: LessonEvent?,
    ): LessonChange? {
        if (oldLesson == newLesson) return null

        return when {
            oldLesson != null && newLesson != null ->
                LessonChange.Modified(old = oldLesson, new = newLesson)
            oldLesson != null -> LessonChange.Removed(oldLesson)
            newLesson != null -> LessonChange.Added(newLesson)
            else -> null
        }
    }
}

sealed class LessonChange {
    data class Modified(
        val old: LessonEvent,
        val new: LessonEvent,
    ) : LessonChange()

    data class Added(
        val new: LessonEvent,
    ) : LessonChange()

    data class Removed(
        val old: LessonEvent,
    ) : LessonChange()
}

data class ScheduleDayChange(
    val date: LocalDate,
    val lessons: List<LessonsByTimeChange>,
)

data class LessonsByTimeChange(
    val time: LessonTime,
    val lessons: List<LessonChange>,
)
