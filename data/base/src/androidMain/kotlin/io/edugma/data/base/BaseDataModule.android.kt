package io.edugma.data.base

import io.edugma.data.base.repository.PathRepositoryImpl
import io.edugma.domain.base.repository.PathRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val baseDataModulePlatform = module {
    factoryOf(::PathRepositoryImpl) { bind<PathRepository>() }
}
