package io.edugma.features.schedule.sources

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.navigation.core.graph.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleSourcesFeatureModule {
    val deps = module {
        factoryOf(::ScheduleSourcesViewModel)
    }

    val screens = screenModule {
        screen(ScheduleScreens.Source) { ScheduleSourcesScreen() }
    }
}
