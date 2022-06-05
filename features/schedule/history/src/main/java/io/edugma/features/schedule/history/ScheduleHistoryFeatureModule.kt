package io.edugma.features.schedule.history

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.schedule.ScheduleHistoryScreens
import io.edugma.features.schedule.history.changes.ScheduleChangesScreen
import io.edugma.features.schedule.history.changes.ScheduleChangesViewModel
import io.edugma.features.schedule.history.main.ScheduleHistoryScreen
import io.edugma.features.schedule.history.main.ScheduleHistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object ScheduleHistoryFeatureModule {
    val deps = module {
        viewModelOf(::ScheduleHistoryViewModel)
        viewModelOf(::ScheduleChangesViewModel)
    }

    val screens = screens {
        addScreen<ScheduleHistoryScreens.Main> { ScheduleHistoryScreen() }
        addScreen<ScheduleHistoryScreens.Changes> {
            ScheduleChangesScreen(
                first = getArg(ScheduleHistoryScreens.Changes::first.name),
                second = getArg(ScheduleHistoryScreens.Changes::second.name)
            )
        }
    }
}