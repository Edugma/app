package com.edugma.android

import com.edugma.core.navigation.core.router.external.ExternalNavigator
import org.koin.dsl.module

val androidModule = module {
    single { params ->
        ExternalNavigator(get(), params.get())
    }
}
