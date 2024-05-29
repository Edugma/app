package com.edugma.core.system

import com.edugma.core.api.repository.MainSnackbarRepository
import com.edugma.core.api.repository.ThemeRepository
import com.edugma.core.system.repository.MainSnackbarRepositoryImpl
import com.edugma.core.system.repository.ThemeRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreSystemModule = module {
    singleOf(::ThemeRepositoryImpl) { bind<ThemeRepository>() }
    singleOf(::MainSnackbarRepositoryImpl) { bind<MainSnackbarRepository>() }
}
