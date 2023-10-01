package io.edugma.core.api

import io.edugma.core.api.utils.AppCoroutineScope
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val baseDomainModule = module {
    singleOf(::AppCoroutineScope)
}
