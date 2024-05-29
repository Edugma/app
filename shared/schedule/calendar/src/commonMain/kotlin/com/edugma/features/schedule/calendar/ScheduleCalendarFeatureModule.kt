package com.edugma.features.schedule.calendar

import com.edugma.core.navigation.ScheduleScreens
import com.edugma.features.schedule.calendar.mapper.CalendarMapper
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleCalendarFeatureModule {
    val deps = module {
        factoryOf(::ScheduleCalendarViewModel)
        factoryOf(::CalendarMapper)
    }

    fun NavGraphBuilder.screens() {
        composeScreen(ScheduleScreens.Calendar) { ScheduleCalendarScreen() }
    }
}
