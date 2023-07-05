package io.edugma.features.schedule.calendar.model

data class CalendarScheduleVO(
    val weekNumber: Int,
    val weekSchedule: List<CalendarDayVO>,
)
