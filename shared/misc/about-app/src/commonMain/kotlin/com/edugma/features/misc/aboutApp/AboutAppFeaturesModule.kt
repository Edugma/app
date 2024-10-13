package com.edugma.features.misc.aboutApp

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val aboutAppFeaturesModule = module {
    factoryOf(::AboutAppViewModel)
}
