package com.edugma.android

import android.app.Application
import com.edugma.core.analytics.CompositeCrashAnalytics
import com.edugma.core.analytics.LogCrashAnalytics
import com.edugma.core.analytics.TracerCrashAnalytics
import com.edugma.core.api.api.CrashAnalytics
import com.edugma.features.app.core.appModules
import com.edugma.shared.app.BuildKonfig
// import com.edugma.features.schedule.appwidget.ScheduleAppwidgetFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.ok.tracer.CoreTracerConfiguration
import ru.ok.tracer.HasTracerConfiguration
import ru.ok.tracer.TracerConfiguration
import ru.ok.tracer.crash.report.CrashFreeConfiguration
import ru.ok.tracer.crash.report.CrashReportConfiguration
import ru.ok.tracer.disk.usage.DiskUsageConfiguration
import ru.ok.tracer.heap.dumps.HeapDumpConfiguration

class App : Application(), HasTracerConfiguration {

    override val tracerConfiguration: List<TracerConfiguration>
        get() = listOf(
            CoreTracerConfiguration.build {
                // опции ядра трейсера
            },
            CrashReportConfiguration.build {
                // опции сборщика крэшей
            },
            CrashFreeConfiguration.build {
                // опции подсчета crash free
            },
            HeapDumpConfiguration.build {
                // опции сборщика хипдампов при ООМ
            },
            DiskUsageConfiguration.build {
                // опции анализатора дискового пространства
            },
        )

    override fun onCreate() {
        super.onCreate()
        setCrashAnalytics()
        // DynamicColors.applyToActivitiesIfAvailable(this)
        startKoin {
            if (BuildKonfig.IsLogsEnabled) {
                androidLogger(Level.ERROR)
            }
            androidContext(this@App)
            modules(appModules + androidModule) // + ScheduleAppwidgetFeatureModule.deps)
        }
    }

    private fun setCrashAnalytics() {
        CrashAnalytics.setInstance(
            CompositeCrashAnalytics(
                delegates = buildList {
                    add(TracerCrashAnalytics())
                    add(LogCrashAnalytics())
                }
            )
        )
    }
}
