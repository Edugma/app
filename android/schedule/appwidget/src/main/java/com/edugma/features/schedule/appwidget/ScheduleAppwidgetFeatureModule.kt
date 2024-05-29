package com.edugma.features.schedule.appwidget

import com.edugma.features.schedule.appwidget.currentLessons.CurrentLessonViewModel
import org.koin.dsl.module

object ScheduleAppwidgetFeatureModule {
    val deps = module {
        factory { CurrentLessonViewModel(get()) }
    }
}
