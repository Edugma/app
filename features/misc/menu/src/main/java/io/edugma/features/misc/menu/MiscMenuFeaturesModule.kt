package io.edugma.features.misc.menu

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val miscMenuFeaturesModule = module {
    factoryOf(::MiscMenuViewModel)
}
