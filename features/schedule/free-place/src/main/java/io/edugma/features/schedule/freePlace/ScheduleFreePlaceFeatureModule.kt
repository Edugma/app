package io.edugma.features.schedule.freePlace

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.ScheduleScreens
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object ScheduleFreePlaceFeatureModule {
    val deps = module {
        viewModelOf(::FreePlaceViewModel)
    }

    val screens = screens {
        addScreen<ScheduleScreens.FreePlace> { FreePlaceScreen() }
    }
}
