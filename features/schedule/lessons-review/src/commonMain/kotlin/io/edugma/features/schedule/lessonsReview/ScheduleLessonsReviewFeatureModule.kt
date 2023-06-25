package io.edugma.features.schedule.lessonsReview

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.navigation.core.graph.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleLessonsReviewFeatureModule {
    val deps = module {
        factoryOf(::LessonsReviewViewModel)
    }

    val screens = screenModule {
        screen(ScheduleScreens.LessonsReview) { LessonsReviewScreen() }
    }
}
