package io.edugma.features.schedule

import io.edugma.features.schedule.free_place.FreePlaceViewModel
import io.edugma.features.schedule.lesson_info.LessonInfoViewModel
import io.edugma.features.schedule.main.ScheduleViewModel
import io.edugma.features.schedule.menu.ScheduleMenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    viewModel { ScheduleMenuViewModel(get()) }
    viewModel { ScheduleViewModel(get()) }
    viewModel { FreePlaceViewModel(get(), get()) }
    viewModel { LessonInfoViewModel() }
}