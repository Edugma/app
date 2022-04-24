package io.edugma.features.schedule.calendar

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    viewModel { ScheduleCalendarViewModel(get()) }
}