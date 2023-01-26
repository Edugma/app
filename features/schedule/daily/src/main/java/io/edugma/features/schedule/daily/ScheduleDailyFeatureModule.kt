package io.edugma.features.schedule.daily

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.ScheduleScreens
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object ScheduleDailyFeatureModule {
    val deps = module {
        viewModelOf(::ScheduleViewModel)
    }

    val screens = screens {
        addScreen<ScheduleScreens.Main> {
            ScheduleScreen(
                date = getArg(ScheduleScreens.Main::date.name),
            )
        }
    }
}
