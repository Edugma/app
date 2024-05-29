package com.edugma.features.schedule.freePlace

import com.edugma.core.navigation.ScheduleScreens
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleFreePlaceFeatureModule {
    val deps = module {
        factoryOf(::FreePlaceViewModel)
    }

    fun NavGraphBuilder.screens() {
        composeScreen(ScheduleScreens.FreePlace) { FreePlaceScreen() }
    }
}
