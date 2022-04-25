package io.edugma.features.schedule.lessons_review

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.ScheduleScreens

val screens = screens {
    addScreen<ScheduleScreens.LessonsReview> { LessonsReviewScreen() }
}