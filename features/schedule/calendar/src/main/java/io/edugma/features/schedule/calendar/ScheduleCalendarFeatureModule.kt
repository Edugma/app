package io.edugma.features.schedule.calendar

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.features.schedule.calendar.mapper.CalendarMapper
import io.edugma.features.schedule.calendar.usecase.GetCurrentDayIndex
import io.edugma.navigation.core.graph.screenModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleCalendarFeatureModule {
    val deps = module {
        viewModelOf(::ScheduleCalendarViewModel)
        factoryOf(::CalendarMapper)
        factoryOf(::GetCurrentDayIndex)
    }

    val screens = screenModule {
        screen(ScheduleScreens.Calendar) { ScheduleCalendarScreen() }
    }
}
