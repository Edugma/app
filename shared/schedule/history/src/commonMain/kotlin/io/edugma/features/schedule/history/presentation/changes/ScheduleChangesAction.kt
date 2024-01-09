package io.edugma.features.schedule.history.presentation.changes

import io.edugma.core.navigation.schedule.ScheduleHistoryScreens
import io.edugma.navigation.core.screen.NavArgs

sealed interface ScheduleChangesAction {
    data class OnArguments(
        val args: NavArgs<ScheduleHistoryScreens.Changes>,
    ) : ScheduleChangesAction
}
