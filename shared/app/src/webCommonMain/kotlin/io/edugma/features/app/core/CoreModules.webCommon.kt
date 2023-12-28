package io.edugma.features.app.core

import io.edugma.core.designSystem.utils.CommonImageLoader
import io.edugma.core.designSystem.utils.IconImageLoader
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val coreModulePlatform = module {
    singleOf(::IconImageLoader)
    singleOf(::CommonImageLoader)
}
