package io.edugma.features.schedule.history

import io.edugma.core.navigation.schedule.ScheduleHistoryScreens
import io.edugma.features.schedule.history.changes.ScheduleChangesScreen
import io.edugma.features.schedule.history.changes.ScheduleChangesViewModel
import io.edugma.features.schedule.history.main.ScheduleHistoryScreen
import io.edugma.features.schedule.history.main.ScheduleHistoryViewModel
import io.edugma.navigation.core.graph.screenModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object ScheduleHistoryFeatureModule {
    val deps = module {
        viewModelOf(::ScheduleHistoryViewModel)
        viewModelOf(::ScheduleChangesViewModel)
    }

    val screens = screenModule {
        screen(ScheduleHistoryScreens.Main) { ScheduleHistoryScreen() }
        screen(ScheduleHistoryScreens.Changes) {
            ScheduleChangesScreen(
                first = screen.first.get(),
                second = screen.second.get(),
            )
        }
    }
}
