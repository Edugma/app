package io.edugma.features.schedule.calendar.model

import androidx.compose.runtime.Immutable

@Immutable
data class CalendarWeekVO(
    val weekNumber: Int = 0,
    val weekSchedule: List<CalendarDayVO>,
)
