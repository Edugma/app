package io.edugma.features.base.core

import io.edugma.core.arch.mvi.viewmodel.ScreenResultProvider
import io.edugma.core.navigation.core.Router
import org.koin.dsl.module

val baseFeaturesModule = module {
    single { Router() }
    single { ScreenResultProvider() }
}
