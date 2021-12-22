package com.mospolytech.features.schedule

import com.mospolytech.features.schedule.lessons_review.LessonsReviewViewModel
import com.mospolytech.features.schedule.main.ScheduleViewModel
import com.mospolytech.features.schedule.menu.ScheduleMenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val scheduleFeaturesModule = module {
    viewModel { ScheduleMenuViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get(), get()) }
    viewModel { LessonsReviewViewModel(get()) }
}