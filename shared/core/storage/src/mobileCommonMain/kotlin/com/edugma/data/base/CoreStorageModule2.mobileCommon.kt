package com.edugma.data.base

import com.edugma.core.api.repository.PreferenceRepository
import com.edugma.data.base.repository.PreferenceRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val coreStorageModulePlatform2: Module = module {
    factoryOf(::PreferenceRepositoryImpl) { bind<PreferenceRepository>() }
}
