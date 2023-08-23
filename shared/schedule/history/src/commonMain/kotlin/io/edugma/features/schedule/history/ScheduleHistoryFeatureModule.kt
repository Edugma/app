package io.edugma.features.schedule.history

import io.edugma.core.navigation.schedule.ScheduleHistoryScreens
import io.edugma.features.schedule.history.presentation.changes.ScheduleChangesScreen
import io.edugma.features.schedule.history.presentation.changes.ScheduleChangesViewModel
import io.edugma.features.schedule.history.presentation.main.ScheduleHistoryScreen
import io.edugma.features.schedule.history.presentation.main.ScheduleHistoryViewModel
import io.edugma.navigation.core.graph.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleHistoryFeatureModule {
    val deps = module {
        factoryOf(::ScheduleHistoryViewModel)
        factoryOf(::ScheduleChangesViewModel)
    }

    val screens = screenModule {
        screen(ScheduleHistoryScreens.Main) { ScheduleHistoryScreen() }
        screen(ScheduleHistoryScreens.Changes) {
            ScheduleChangesScreen(args = this)
        }
    }
}
