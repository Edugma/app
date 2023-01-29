package io.edugma.features.schedule.lessonsReview

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.ScheduleScreens
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ScheduleLessonsReviewFeatureModule {
    val deps = module {
        viewModel { LessonsReviewViewModel(get()) }
    }

    val screens = screens {
        addScreen<ScheduleScreens.LessonsReview> { LessonsReviewScreen() }
    }
}
