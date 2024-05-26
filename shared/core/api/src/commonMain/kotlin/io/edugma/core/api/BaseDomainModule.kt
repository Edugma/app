package io.edugma.core.api

import io.edugma.core.api.repository.AppStateRepository
import io.edugma.core.api.repository.AppStateRepositoryImpl
import io.edugma.core.api.utils.AppCoroutineScope
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val baseDomainModule = module {
    singleOf(::AppCoroutineScope)
    singleOf(::AppStateRepositoryImpl) { bind<AppStateRepository>() }
}
