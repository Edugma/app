package io.edugma.features.schedule.sources

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    viewModel { ScheduleSourcesViewModel(get()) }
}