package com.edugma.features.app.core

import com.edugma.core.navigation.core.router.external.ExternalNavigator
import org.koin.dsl.module

val iosModule = module {
    single { params ->
        ExternalNavigator(get(), params.get())
    }
}
