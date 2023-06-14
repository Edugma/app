package io.edugma.features.app.di

import io.edugma.core.arch.mvi.viewmodel.ScreenResultProvider
import io.edugma.core.designSystem.utils.CommonImageLoader
import io.edugma.core.designSystem.utils.IconImageLoader
import io.edugma.core.navigation.core.Router
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::IconImageLoader)
    singleOf(::CommonImageLoader)
    singleOf(::Router)
    singleOf(::ScreenResultProvider)
}
