package io.edugma.features.schedule.menu

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.ScheduleScreens
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object ScheduleMenuFeatureModule {
    val deps = module {
        viewModelOf(::ScheduleMenuViewModel)
    }

    val screens = screens {
        addScreen<ScheduleScreens.Menu> { ScheduleMenuScreen() }
    }
}