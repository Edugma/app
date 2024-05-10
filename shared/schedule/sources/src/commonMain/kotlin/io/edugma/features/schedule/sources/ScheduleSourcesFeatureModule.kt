package io.edugma.features.schedule.sources

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.graph.composeScreen
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleSourcesFeatureModule {
    val deps = module {
        factoryOf(::ScheduleSourcesViewModel)
    }

    fun NavGraphBuilder.screens() {
        composeScreen(ScheduleScreens.Source) { ScheduleSourcesScreen() }
    }
}
