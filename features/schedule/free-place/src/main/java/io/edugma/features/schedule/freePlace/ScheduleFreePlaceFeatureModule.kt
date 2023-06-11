package io.edugma.features.schedule.freePlace

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.navigation.core.graph.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleFreePlaceFeatureModule {
    val deps = module {
        factoryOf(::FreePlaceViewModel)
    }

    val screens = screenModule {
        screen(ScheduleScreens.FreePlace) { FreePlaceScreen() }
    }
}
