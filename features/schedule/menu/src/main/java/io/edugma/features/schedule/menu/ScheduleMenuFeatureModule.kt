package io.edugma.features.schedule.menu

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.ScheduleScreens
import io.edugma.features.schedule.menu.usecase.GetScheduleMenuItems
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleMenuFeatureModule {
    val deps = module {
        viewModelOf(::ScheduleMenuViewModel)
        factoryOf(::GetScheduleMenuItems)
    }

    val screens = screens {
        addScreen<ScheduleScreens.Menu> { ScheduleMenuScreen() }
    }
}
