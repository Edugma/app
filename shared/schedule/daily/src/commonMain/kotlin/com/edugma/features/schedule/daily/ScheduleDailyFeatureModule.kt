package com.edugma.features.schedule.daily

import com.edugma.core.navigation.ScheduleScreens
import com.edugma.features.schedule.daily.presentation.ScheduleScreen
import com.edugma.features.schedule.daily.presentation.ScheduleViewModel
import com.edugma.navigation.core.compose.rememberNavArgs
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen
import kotlinx.datetime.LocalDate
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleDailyFeatureModule {
    val deps = module {
        factoryOf(::ScheduleViewModel)
    }

    fun NavGraphBuilder.screens() {
        composeScreen(ScheduleScreens.Main) {
            val args = rememberNavArgs(ScheduleScreens.Main)

            val epochDays = args { destination.date.get() }.takeIf { it != 0 }
            val date = epochDays?.let { LocalDate.fromEpochDays(it) }

            ScheduleScreen(
                date = date,
            )
        }
    }
}
