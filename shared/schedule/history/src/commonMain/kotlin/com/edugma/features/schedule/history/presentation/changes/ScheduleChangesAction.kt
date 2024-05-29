package com.edugma.features.schedule.history.presentation.changes

import com.edugma.core.navigation.schedule.ScheduleHistoryScreens
import com.edugma.navigation.core.destination.NavArgs

sealed interface ScheduleChangesAction {
    data class OnArguments(
        val args: NavArgs<ScheduleHistoryScreens.Changes>,
    ) : ScheduleChangesAction
}
