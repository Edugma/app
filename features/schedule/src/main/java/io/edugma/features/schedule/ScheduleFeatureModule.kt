package io.edugma.features.schedule

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.ScheduleScreens
import io.edugma.features.schedule.free_place.FreePlaceScreen
import io.edugma.features.schedule.free_place.FreePlaceViewModel
import io.edugma.features.schedule.lesson_info.LessonInfoScreen
import io.edugma.features.schedule.lesson_info.LessonInfoViewModel
import io.edugma.features.schedule.main.ScheduleScreen
import io.edugma.features.schedule.main.ScheduleViewModel
import io.edugma.features.schedule.menu.ScheduleMenuScreen
import io.edugma.features.schedule.menu.ScheduleMenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ScheduleFeatureModule {
    val deps = module {
        viewModel { ScheduleMenuViewModel(get()) }
        viewModel { ScheduleViewModel(get()) }
        viewModel { FreePlaceViewModel(get(), get()) }
        viewModel { LessonInfoViewModel() }
    }

    val screens = screens {
        addScreen<ScheduleScreens.Menu> { ScheduleMenuScreen() }
        addScreen<ScheduleScreens.Main> {
            ScheduleScreen(
                date = getArg(ScheduleScreens.Main::date.name)
            )
        }
        addScreen<ScheduleScreens.FreePlace> { FreePlaceScreen() }
        addScreen<ScheduleScreens.LessonInfo> {
            LessonInfoScreen(
                lessonInfo = getArg(ScheduleScreens.LessonInfo::lessonInfo.name)
            )
        }
    }
}