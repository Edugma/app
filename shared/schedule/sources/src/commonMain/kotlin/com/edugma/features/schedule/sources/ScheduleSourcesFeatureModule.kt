package com.edugma.features.schedule.sources

import com.edugma.core.navigation.ScheduleScreens
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen
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
