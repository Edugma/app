package com.edugma.features.schedule.history

import com.edugma.core.navigation.schedule.ScheduleHistoryScreens
import com.edugma.features.schedule.history.presentation.changes.ScheduleChangesScreen
import com.edugma.features.schedule.history.presentation.changes.ScheduleChangesViewModel
import com.edugma.features.schedule.history.presentation.main.ScheduleHistoryScreen
import com.edugma.features.schedule.history.presentation.main.ScheduleHistoryViewModel
import com.edugma.navigation.core.compose.rememberNavArgs
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen
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
