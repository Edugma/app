package io.edugma.features.schedule.daily

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.navigation.core.graph.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleDailyFeatureModule {
    val deps = module {
        factoryOf(::ScheduleViewModel)
    }

    val screens = screenModule {
        screen(ScheduleScreens.Main) {
            ScheduleScreen(
                date = screen.date.get(),
            )
        }
    }
}
