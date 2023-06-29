package io.edugma.features.app.di

import io.edugma.core.arch.mvi.viewmodel.ScreenResultProvider
import io.edugma.core.navigation.core.Router
import io.edugma.core.navigation.core.router.external.ExternalRouter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::Router)
    singleOf(::ScreenResultProvider)
    singleOf(::ExternalRouter)
} + coreModulePlatform
