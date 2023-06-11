package io.edugma.features.schedule.lessonsReview

import io.edugma.core.navigation.ScheduleScreens
import io.edugma.navigation.core.graph.screenModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ScheduleLessonsReviewFeatureModule {
    val deps = module {
        viewModel { LessonsReviewViewModel(get()) }
    }

    val screens = screenModule {
        screen(ScheduleScreens.LessonsReview) { LessonsReviewScreen() }
    }
}
