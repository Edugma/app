package com.edugma.core.api

import com.edugma.core.api.repository.AppStateRepository
import com.edugma.core.api.repository.AppStateRepositoryImpl
import com.edugma.core.api.utils.AppCoroutineScope
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val baseDomainModule = module {
    singleOf(::AppCoroutineScope)
    singleOf(::AppStateRepositoryImpl) { bind<AppStateRepository>() }
}
