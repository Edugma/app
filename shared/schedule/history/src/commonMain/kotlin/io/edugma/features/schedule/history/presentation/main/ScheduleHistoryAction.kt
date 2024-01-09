package io.edugma.features.schedule.history.presentation.main

import kotlinx.datetime.Instant

sealed interface ScheduleHistoryAction {
    data class OnScheduleSelected(val timestamp: Instant) : ScheduleHistoryAction
    data object OnCompareClicked : ScheduleHistoryAction
    data object OnBack : ScheduleHistoryAction
}
