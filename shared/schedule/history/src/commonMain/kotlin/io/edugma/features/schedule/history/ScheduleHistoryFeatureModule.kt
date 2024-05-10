package io.edugma.features.schedule.history

import io.edugma.core.navigation.schedule.ScheduleHistoryScreens
import io.edugma.features.schedule.history.presentation.changes.ScheduleChangesScreen
import io.edugma.features.schedule.history.presentation.changes.ScheduleChangesViewModel
import io.edugma.features.schedule.history.presentation.main.ScheduleHistoryScreen
import io.edugma.features.schedule.history.presentation.main.ScheduleHistoryViewModel
import io.edugma.navigation.core.compose.rememberNavArgs
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.graph.composeScreen
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleHistoryFeatureModule {
    val deps = module {
        factoryOf(::ScheduleHistoryViewModel)
        factoryOf(::ScheduleChangesViewModel)
    }

    fun NavGraphBuilder.screens() {
        composeScreen(ScheduleHistoryScreens.Main) { ScheduleHistoryScreen() }
        composeScreen(ScheduleHistoryScreens.Changes) {
            val args = rememberNavArgs(ScheduleHistoryScreens.Changes)

            ScheduleChangesScreen(args = args)
        }
    }
}
