package io.edugma.features.app.core

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModules)
    }
}
