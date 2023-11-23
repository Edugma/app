package io.edugma.data.base

import io.edugma.core.api.repository.PathRepository
import io.edugma.data.base.repository.PathRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val coreStorageModulePlatform = module {
    factoryOf(::PathRepositoryImpl) { bind<PathRepository>() }
}
