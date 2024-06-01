package com.edugma.features.schedule.calendar.model

import androidx.compose.runtime.Immutable
import com.edugma.core.api.utils.DateFormat
import com.edugma.core.api.utils.format
import com.edugma.core.api.utils.getIsoWeekNumber
import com.edugma.core.api.utils.isLeapYear
import com.edugma.features.schedule.domain.model.schedule.ScheduleWeeksCalendar

@Immutable
data class ScheduleCalendarVO(
    private val scheduleWeeksCalendar: ScheduleWeeksCalendar,
) {
    val size: Int
        get() = scheduleWeeksCalendar.size

    operator fun get(index: Int): CalendarWeekVO {
        val days = scheduleWeeksCalendar.getWeek(index).map { scheduleDay ->

            CalendarDayVO(
                dayTitle = scheduleDay.date.format(DateFormat.FULL),
                date = scheduleDay.date,
                isToday = scheduleDay.isToday,
                lessons = scheduleDay.lessons.map { it.toCalendarLesson() },
            )
        }
        val weekNumber = days.firstOrNull()?.date?.getIsoWeekNumber() ?: 0

        return CalendarWeekVO(
            weekSchedule = days,
            weekNumber = weekNumber,
        )
    }
}
