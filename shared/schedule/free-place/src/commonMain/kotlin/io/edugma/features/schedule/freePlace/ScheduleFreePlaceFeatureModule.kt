package io.edugma.features.schedule.freePlace

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.graph.composeScreen
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
