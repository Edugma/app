package io.edugma.features.schedule.history

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.schedule.ScheduleHistoryScreens
import io.edugma.features.schedule.history.main.ScheduleHistoryScreen
import io.edugma.features.schedule.history.main.ScheduleHistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ScheduleHistoryFeatureModule {
    val deps = module {
        viewModel { ScheduleHistoryViewModel(get()) }
    }

    val screens = screens {
        addScreen<ScheduleHistoryScreens.Main> { ScheduleHistoryScreen() }
    }
}