package io.edugma.android

import android.app.Application
import io.edugma.features.app.core.appModules
import io.edugma.shared.app.BuildKonfig
// import io.edugma.features.schedule.appwidget.ScheduleAppwidgetFeatureModule
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
