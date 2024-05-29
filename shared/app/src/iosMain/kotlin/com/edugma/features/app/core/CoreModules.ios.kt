package com.edugma.features.app.core

import com.edugma.core.designSystem.utils.CommonImageLoader
import com.edugma.core.designSystem.utils.IconImageLoader
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val coreModulePlatform = module {
    singleOf(::IconImageLoader)
    singleOf(::CommonImageLoader)
}
