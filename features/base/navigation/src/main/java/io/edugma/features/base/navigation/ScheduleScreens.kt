package io.edugma.features.base.navigation

import io.edugma.features.base.core.navigation.core.Screen

object ScheduleScreens {

    object Menu : Screen()

    object Main : Screen()

    object Source : Screen()

    object Calendar : Screen()

    object LessonsReview : Screen()

    object FreePlace : Screen()

    class LessonInfo(
        val lessonInfo: io.edugma.domain.schedule.model.lesson.LessonInfo
    ) : Screen(
        LessonInfo::lessonInfo.name to lessonInfo.serialized()
    )
}