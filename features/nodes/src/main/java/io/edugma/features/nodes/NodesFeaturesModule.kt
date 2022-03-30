package io.edugma.features.nodes

import io.edugma.features.nodes.main.NodesMainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val nodesFeaturesModule = module {
    viewModel { NodesMainViewModel(get()) }
}