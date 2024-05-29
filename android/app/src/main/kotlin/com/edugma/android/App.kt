package com.edugma.android

import android.app.Application
import com.edugma.features.app.core.appModules
import com.edugma.shared.app.BuildKonfig
// import com.edugma.features.schedule.appwidget.ScheduleAppwidgetFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // DynamicColors.applyToActivitiesIfAvailable(this)
        startKoin {
            if (BuildKonfig.IsLogsEnabled) {
                androidLogger(Level.ERROR)
            }
            androidContext(this@App)
            modules(appModules + androidModule) // + ScheduleAppwidgetFeatureModule.deps)
        }
    }
}
