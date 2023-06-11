package io.edugma.features.schedule.sources

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.navigation.core.graph.screenModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ScheduleSourcesFeatureModule {
    val deps = module {
        viewModel { ScheduleSourcesViewModel(get()) }
    }

    val screens = screenModule {
        screen(ScheduleScreens.Source) { ScheduleSourcesScreen() }
    }
}
