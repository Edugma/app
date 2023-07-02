package io.edugma.features.app.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModules)
    }
}
