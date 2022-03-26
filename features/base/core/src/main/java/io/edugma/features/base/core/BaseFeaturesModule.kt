package io.edugma.features.base.core

import io.edugma.features.base.core.navigation.core.Router
import org.koin.dsl.module

val baseFeaturesModule = module {
    single { Router() }
}
