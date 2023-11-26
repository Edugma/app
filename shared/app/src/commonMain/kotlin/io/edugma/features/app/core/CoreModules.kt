package io.edugma.features.app.core

import io.edugma.core.api.repository.BuildConfigRepository
import io.edugma.core.arch.mvi.viewmodel.ScreenResultProvider
import io.edugma.core.arch.pagination.PagingViewModel
import io.edugma.core.navigation.core.Router
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.features.app.data.BuildConfigRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::Router)
    singleOf(::ScreenResultProvider)
    singleOf(::ExternalRouter)
    singleOf(::BuildConfigRepositoryImpl) { bind<BuildConfigRepository>() }
    factoryOf<PagingViewModel<Any>>(::PagingViewModel)
} + coreModulePlatform
