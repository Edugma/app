package io.edugma.features.schedule.calendar

import androidx.compose.runtime.Immutable
import io.edugma.core.api.model.LceUiState
import io.edugma.features.schedule.calendar.model.CalendarScheduleVO

@Immutable
data class ScheduleCalendarUiState(
    val lceState: LceUiState = LceUiState.init(),
    val schedule: List<CalendarScheduleVO> = emptyList(),
    val currentWeekIndex: Int = -1,
    val currentDayOfWeekIndex: Int = -1,
)
