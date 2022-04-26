package io.edugma.features.schedule.sources

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.ScheduleScreens
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ScheduleSourcesFeatureModule {
    val module = module {
        viewModel { ScheduleSourcesViewModel(get()) }
    }

    val screens = screens {
        addScreen<ScheduleScreens.Source> { ScheduleSourcesScreen() }
    }
}