package io.edugma.core.system

import io.edugma.core.api.repository.ThemeRepository
import io.edugma.core.system.repository.ThemeRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreSystemModule = module {
    singleOf(::ThemeRepositoryImpl) { bind<ThemeRepository>() }
}
