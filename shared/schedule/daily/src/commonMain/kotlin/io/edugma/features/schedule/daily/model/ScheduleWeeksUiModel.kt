package io.edugma.features.schedule.daily.model

import io.edugma.features.schedule.domain.model.schedule.ScheduleWeeksCalendar

class ScheduleWeeksUiModel(
    private val scheduleWeeksCalendar: ScheduleWeeksCalendar,
) {
    val size: Int
        get() = scheduleWeeksCalendar.size

    operator fun get(index: Int): WeekUiModel {
        val days = scheduleWeeksCalendar.getWeek(index).map { scheduleDay ->

            DayUiModel(
                date = scheduleDay.date,
                isToday = scheduleDay.isToday,
                lessonCount = scheduleDay.lessons.size,
            )
        }
        return WeekUiModel(
            days = days,
        )
    }
}
