package io.edugma.features.schedule.daily

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.features.schedule.daily.presentation.ScheduleScreen
import io.edugma.features.schedule.daily.presentation.ScheduleViewModel
import io.edugma.navigation.core.compose.rememberNavArgs
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.graph.composeScreen
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
