package io.edugma.data.schedule.model

import io.edugma.domain.schedule.model.compact.CompactLessonFeatures
import io.edugma.domain.schedule.model.compact.CompactSchedule
import io.edugma.domain.schedule.model.compact.ScheduleInfo
import io.edugma.domain.schedule.model.group.Group
import io.edugma.domain.schedule.model.group.description
import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonDateTime
import io.edugma.domain.schedule.model.lesson.LessonDateTimes
import io.edugma.domain.schedule.model.lesson.LessonTime
import io.edugma.domain.schedule.model.lesson_subject.LessonSubject
import io.edugma.domain.schedule.model.lesson_type.LessonType
import io.edugma.domain.schedule.model.place.Place
import io.edugma.domain.schedule.model.place.description
import io.edugma.domain.schedule.model.schedule.LessonsByTime
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.teacher.Teacher
import io.edugma.domain.schedule.model.teacher.description
import java.time.LocalDate
import java.util.*

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

fun CompactLessonFeatures.toModel(info: ScheduleInfo): Lesson {
    return Lesson(
        subject = info.subjectsInfo.first { it.id == subjectId }.let {
            LessonSubject(
                id = it.id,
                title = it.title
            )
        },
        type = info.typesInfo.first { it.id == typeId }.let {
            LessonType(
                id = it.id,
                title = it.title,
                isImportant = it.isImportant
            )
        },
        teachers = teachersId.map { id ->
            val temp = info.teachersInfo.first { it.id == id }
            Teacher(
                id = temp.id,
                name = temp.name,
                description = temp.description
            )
        },
        groups = groupsId.map { id ->
            val temp = info.groupsInfo.first { it.id == id }
            Group(
                id = temp.id,
                title = temp.title,
                description = temp.description
            )
        },
        places = placesId.map { id ->
            val temp = info.placesInfo.first { it.id == id }
            Place(
                id = temp.id,
                title = temp.title,
                type = temp.type,
                description = temp.description
            )
        },
    )
}

data class LocalLessonDateTime(
    val date: LocalDate,
    val time: LessonTime
)