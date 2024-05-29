package com.edugma.core.utils

import com.edugma.core.utils.viewmodel.DefaultErrorHandler
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreUtilsModule = module {
    factoryOf(::DefaultErrorHandler)
}
