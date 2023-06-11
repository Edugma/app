package io.edugma.features.schedule.menu

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.features.schedule.menu.usecase.GetScheduleMenuItems
import io.edugma.navigation.core.graph.screenModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleMenuFeatureModule {
    val deps = module {
        viewModelOf(::ScheduleMenuViewModel)
        factoryOf(::GetScheduleMenuItems)
    }

    val screens = screenModule {
        screen(ScheduleScreens.Menu) { ScheduleMenuScreen() }
    }
}
