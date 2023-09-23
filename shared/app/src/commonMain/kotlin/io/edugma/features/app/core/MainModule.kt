package io.edugma.features.app.core

import io.edugma.features.app.presentation.main.MainAppViewModel
import io.edugma.features.app.presentation.main.MainViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val mainModule = module {
    factoryOf(::MainViewModel)
    factoryOf(::MainAppViewModel)
}
