package io.edugma.features.schedule.appwidget

import io.edugma.features.schedule.appwidget.currentLessons.CurrentLessonViewModel
import org.koin.dsl.module

object ScheduleAppwidgetFeatureModule {
    val deps = module {
        factory { CurrentLessonViewModel(get()) }
    }
}
