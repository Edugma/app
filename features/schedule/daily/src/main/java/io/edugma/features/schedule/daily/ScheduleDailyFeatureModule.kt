package io.edugma.features.schedule.daily

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.navigation.core.graph.screenModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object ScheduleDailyFeatureModule {
    val deps = module {
        viewModelOf(::ScheduleViewModel)
    }

    val screens = screenModule {
        screen(ScheduleScreens.Main) {
            ScheduleScreen(
                date = screen.date.get(),
            )
        }
    }
}
