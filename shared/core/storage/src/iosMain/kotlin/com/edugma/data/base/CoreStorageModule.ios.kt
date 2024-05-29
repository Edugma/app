package com.edugma.data.base

import com.edugma.core.api.repository.PathRepository
import com.edugma.data.base.repository.PathRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val coreStorageModulePlatform = module {
    factoryOf(::PathRepositoryImpl) { bind<PathRepository>() }
}
