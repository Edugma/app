package io.edugma.features.schedule.calendar

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.ScheduleScreens
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ScheduleCalendarFeatureModule {
    val module = module {
        viewModel { ScheduleCalendarViewModel(get()) }
    }

    val screens = screens {
        addScreen<ScheduleScreens.Calendar> { ScheduleCalendarScreen() }
    }
}