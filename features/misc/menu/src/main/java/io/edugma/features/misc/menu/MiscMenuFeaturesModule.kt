package io.edugma.features.misc.menu

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val miscMenuFeaturesModule = module {
    viewModel { MiscMenuViewModel() }
}