package io.edugma.features.schedule

import io.edugma.features.schedule.calendar.ScheduleCalendarViewModel
import io.edugma.features.schedule.free_place.FreePlaceViewModel
import io.edugma.features.schedule.lesson_info.LessonInfoViewModel
import io.edugma.features.schedule.lessons_review.LessonsReviewViewModel
import io.edugma.features.schedule.main.ScheduleViewModel
import io.edugma.features.schedule.menu.ScheduleMenuViewModel
import io.edugma.features.schedule.sources.ScheduleSourcesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    viewModel { ScheduleMenuViewModel(get()) }
    viewModel { ScheduleViewModel(get()) }
    viewModel { LessonsReviewViewModel(get()) }
    viewModel { ScheduleCalendarViewModel(get()) }
    viewModel { FreePlaceViewModel(get(), get()) }
    viewModel { ScheduleSourcesViewModel(get()) }
    viewModel { LessonInfoViewModel() }
}