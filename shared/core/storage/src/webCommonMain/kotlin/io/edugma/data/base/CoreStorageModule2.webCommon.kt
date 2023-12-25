package io.edugma.data.base

import io.edugma.core.api.repository.PreferenceRepository
import io.edugma.data.base.repository.PreferenceRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val coreStorageModulePlatform2: Module = module {
    factoryOf(::PreferenceRepositoryImpl) { bind<PreferenceRepository>() }
}
