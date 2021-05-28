package com.mospolytech.mospolyhelper.domain.schedule.model

import com.mospolytech.mospolyhelper.domain.schedule.utils.filterByDate
import com.mospolytech.mospolyhelper.utils.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Schedule(
    val dailySchedules: List<List<LessonPlace>>,
    @Serializable(with = LocalDateSerializer::class)
    val dateFrom: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val dateTo: LocalDate
) {
    companion object {
        fun from(dailySchedules: List<List<LessonPlace>>): Schedule {
            var dateFrom = LocalDate.MAX
            var dateTo = LocalDate.MIN
            for (dailySchedule in dailySchedules) {
                for (lessonPlace in dailySchedule) {
                    for (lesson in lessonPlace.lessons) {
                        if (lesson.dateFrom < dateFrom)
                            dateFrom = lesson.dateFrom
                        if (lesson.dateTo > dateTo)
                            dateTo = lesson.dateTo
                    }
                }
            }

            return Schedule(
                dailySchedules,
                dateFrom,
                dateTo
            )
        }
    }

    fun getLessons(
        date: LocalDate,
        showEnded: Boolean = false,
        showCurrent: Boolean = true,
        showNotStarted: Boolean = false
    ) = filterByDate(
        dailySchedules[date.dayOfWeek.value % 7],
        date,
        showEnded,
        showCurrent,
        showNotStarted
    )

    fun getLessons(
        date: LocalDate,
        lessonDateFilter: LessonDateFilter = LessonDateFilter.Default
    ) = filterByDate(
        dailySchedules[date.dayOfWeek.value % 7],
        date,
        lessonDateFilter.showEndedLessons,
        lessonDateFilter.showCurrentLessons,
        lessonDateFilter.showNotStartedLessons
    )
}

class SchedulePackList(
    val schedules: Iterable<Schedule?>,
    val lessonTitles: List<String>,
    val lessonTypes: List<String>,
    val lessonTeachers: List<String>,
    val lessonGroups: List<String>,
    val lessonAuditoriums: List<String>,
)