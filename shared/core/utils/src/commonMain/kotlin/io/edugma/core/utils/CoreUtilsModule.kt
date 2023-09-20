package io.edugma.core.utils

import io.edugma.core.utils.viewmodel.DefaultErrorHandler
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreUtilsModule = module {
    factoryOf(::DefaultErrorHandler)
}
