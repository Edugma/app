package io.edugma.android

import io.edugma.core.navigation.core.router.external.ExternalNavigator
import org.koin.dsl.module

val androidModule = module {
    single { params ->
        ExternalNavigator(get(), params.get())
    }
}
