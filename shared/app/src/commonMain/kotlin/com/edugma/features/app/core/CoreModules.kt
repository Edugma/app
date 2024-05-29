package com.edugma.features.app.core

import com.edugma.core.api.repository.BuildConfigRepository
import com.edugma.core.arch.mvi.viewmodel.ScreenResultProvider
import com.edugma.core.arch.pagination.PagingViewModel
import com.edugma.core.navigation.core.AccountRouter
import com.edugma.core.navigation.core.HomeRouter
import com.edugma.core.navigation.core.MiscRouter
import com.edugma.core.navigation.core.ScheduleRouter
import com.edugma.core.navigation.core.TabMenuRouter
import com.edugma.core.navigation.core.router.external.ExternalRouter
import com.edugma.features.app.data.BuildConfigRepositoryImpl
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
