package com.edugma.features.schedule.menu

import com.edugma.core.navigation.ScheduleScreens
import com.edugma.features.schedule.menu.presentation.ScheduleMenuScreen
import com.edugma.features.schedule.menu.presentation.ScheduleMenuViewModel
import com.edugma.features.schedule.menu.usecase.GetScheduleMenuItems
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleMenuFeatureModule {
    val deps = module {
        factoryOf(::ScheduleMenuViewModel)
        factoryOf(::GetScheduleMenuItems)
    }

    fun NavGraphBuilder.screens() {
        composeScreen(ScheduleScreens.Menu) { ScheduleMenuScreen() }
    }
}
