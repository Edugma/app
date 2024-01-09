package io.edugma.features.schedule.calendar

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.features.schedule.calendar.mapper.CalendarMapper
import io.edugma.features.schedule.calendar.usecase.GetCurrentDayIndexUseCase
import io.edugma.navigation.core.graph.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleCalendarFeatureModule {
    val deps = module {
        factoryOf(::ScheduleCalendarViewModel)
        factoryOf(::CalendarMapper)
        factoryOf(::GetCurrentDayIndexUseCase)
    }

    val screens = screenModule {
        screen(ScheduleScreens.Calendar) { ScheduleCalendarScreen() }
    }
}
