package io.edugma.features.schedule.schedule_info

import io.edugma.features.schedule.schedule_info.place_info.PlaceInfoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    viewModel { PlaceInfoViewModel(get(), get()) }
}