package io.edugma.features.schedule.menu

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.features.schedule.menu.presentation.ScheduleMenuScreen
import io.edugma.features.schedule.menu.presentation.ScheduleMenuViewModel
import io.edugma.features.schedule.menu.usecase.GetScheduleMenuItems
import io.edugma.navigation.core.graph.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleMenuFeatureModule {
    val deps = module {
        factoryOf(::ScheduleMenuViewModel)
        factoryOf(::GetScheduleMenuItems)
    }

    val screens = screenModule {
        screen(ScheduleScreens.Menu) { ScheduleMenuScreen() }
    }
}
