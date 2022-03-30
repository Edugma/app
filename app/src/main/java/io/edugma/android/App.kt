package io.edugma.android

import android.app.Application
import io.edugma.features.base.core.utils.androidSharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            androidSharedPreferences(this@App, io.edugma.android.BuildConfig.APPLICATION_ID)
            modules(appModules)
        }
    }
}