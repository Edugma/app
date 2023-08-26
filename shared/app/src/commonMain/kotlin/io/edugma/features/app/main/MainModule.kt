package io.edugma.features.app.main

import io.edugma.features.app.MainAppViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val mainModule = module {
    factoryOf(::MainViewModel)
    factoryOf(::MainAppViewModel)
}
