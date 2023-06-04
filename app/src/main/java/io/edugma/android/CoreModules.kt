package io.edugma.android

import io.edugma.core.designSystem.utils.CommonImageLoader
import io.edugma.core.designSystem.utils.IconImageLoader
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::IconImageLoader)
    singleOf(::CommonImageLoader)
}
