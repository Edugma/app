package com.edugma.features.schedule.calendar.model

import androidx.compose.runtime.Immutable

@Immutable
data class CalendarWeekVO(
    val weekNumber: Int,
    val weekSchedule: List<CalendarDayVO>,
)
