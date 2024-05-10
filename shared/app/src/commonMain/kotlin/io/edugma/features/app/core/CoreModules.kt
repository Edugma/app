package io.edugma.features.app.core

import io.edugma.core.api.repository.BuildConfigRepository
import io.edugma.core.arch.mvi.viewmodel.ScreenResultProvider
import io.edugma.core.arch.pagination.PagingViewModel
import io.edugma.core.navigation.core.AccountRouter
import io.edugma.core.navigation.core.HomeRouter
import io.edugma.core.navigation.core.MiscRouter
import io.edugma.core.navigation.core.ScheduleRouter
import io.edugma.core.navigation.core.TabMenuRouter
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.features.app.data.BuildConfigRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::TabMenuRouter)
    singleOf(::HomeRouter)
    singleOf(::ScheduleRouter)
    singleOf(::AccountRouter)
    singleOf(::MiscRouter)
    singleOf(::ScreenResultProvider)
    singleOf(::ExternalRouter)
    singleOf(::BuildConfigRepositoryImpl) { bind<BuildConfigRepository>() }
    factoryOf<PagingViewModel<Any>>(::PagingViewModel)
} + coreModulePlatform
