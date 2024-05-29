package com.edugma.features.schedule.lessonsReview

import com.edugma.core.navigation.ScheduleScreens
import com.edugma.features.schedule.lessonsReview.list.domain.LessonsReviewUseCase
import com.edugma.features.schedule.lessonsReview.list.presentation.LessonsReviewScreen
import com.edugma.features.schedule.lessonsReview.list.presentation.LessonsReviewViewModel
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleLessonsReviewFeatureModule {
    val deps = module {
        factoryOf(::LessonsReviewViewModel)
        factoryOf(::LessonsReviewUseCase)
    }

    fun NavGraphBuilder.screens() {
        composeScreen(ScheduleScreens.LessonsReview) { LessonsReviewScreen() }
    }
}
