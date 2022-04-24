package io.edugma.features.schedule

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.ScheduleScreens
import io.edugma.features.schedule.free_place.FreePlaceScreen
import io.edugma.features.schedule.lessons_review.LessonsReviewScreen
import io.edugma.features.schedule.main.ScheduleScreen
import io.edugma.features.schedule.menu.ScheduleMenuScreen
import io.edugma.features.schedule.lesson_info.LessonInfoScreen
import io.edugma.features.schedule.sources.ScheduleSourcesScreen

val screens = screens {
    addScreen<ScheduleScreens.Menu> { ScheduleMenuScreen() }
    addScreen<ScheduleScreens.Main> { ScheduleScreen() }
    addScreen<ScheduleScreens.LessonsReview> { LessonsReviewScreen() }
    addScreen<ScheduleScreens.Source> { ScheduleSourcesScreen() }
    addScreen<ScheduleScreens.FreePlace> { FreePlaceScreen() }
    addScreen<ScheduleScreens.LessonInfo> {
        LessonInfoScreen(
            lessonInfo = getArg(ScheduleScreens.LessonInfo::lessonInfo.name)
        )
    }
}