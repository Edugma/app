package io.edugma.features.schedule.calendar

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.ScheduleScreens
import io.edugma.features.schedule.calendar.mapper.CalendarMapper
import io.edugma.features.schedule.calendar.usecase.GetCurrentDayIndex
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleCalendarFeatureModule {
    val deps = module {
        viewModelOf(::ScheduleCalendarViewModel)
        factoryOf(::CalendarMapper)
        factoryOf(::GetCurrentDayIndex)
    }

    val screens = screens {
        addScreen<ScheduleScreens.Calendar> { ScheduleCalendarScreen() }
    }
}
