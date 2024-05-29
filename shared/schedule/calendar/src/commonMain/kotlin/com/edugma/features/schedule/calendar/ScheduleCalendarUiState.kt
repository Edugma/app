package com.edugma.features.schedule.calendar

import androidx.compose.runtime.Immutable
import com.edugma.core.api.model.LceUiState
import com.edugma.features.schedule.calendar.model.ScheduleCalendarVO
import com.edugma.features.schedule.domain.model.schedule.CalendarSettings

@Immutable
data class ScheduleCalendarUiState(
    val lceState: LceUiState = LceUiState.init(),
    val schedule: ScheduleCalendarVO? = null,
    val currentWeekIndex: Int = -1,
    val currentDayOfWeekIndex: Int = -1,
    val settings: CalendarSettings = CalendarSettings.Infinity,
)
