package com.edugma.features.app.core

import com.edugma.core.api.repository.CleanupRepository
import com.edugma.features.app.data.CleanupRepositoryImpl
import com.edugma.features.app.presentation.main.MainAppViewModel
import com.edugma.features.app.presentation.main.MainViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val mainModule = module {
    factoryOf(::MainViewModel)
    factoryOf(::MainAppViewModel)
    singleOf(::CleanupRepositoryImpl) { bind<CleanupRepository>() }
}
