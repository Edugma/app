package io.edugma.features.schedule.calendar

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.features.schedule.calendar.mapper.CalendarMapper
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.graph.composeScreen
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
