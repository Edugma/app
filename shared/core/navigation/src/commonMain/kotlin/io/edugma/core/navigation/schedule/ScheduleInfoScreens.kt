package io.edugma.core.navigation.schedule

import io.edugma.navigation.core.screen.Screen
import io.edugma.navigation.core.screen.bundleOf
import io.edugma.navigation.core.screen.set

object ScheduleInfoScreens {
    object LessonInfo : Screen("scheduleInfoLesson") {
        val lessonInfo = reqArg<String>("lessonInfo")

        operator fun invoke(
            lessonInfo: String,
        ) = bundleOf(this.lessonInfo set lessonInfo)
    }

    object SubjectInfo : Screen("scheduleInfoSubjectInfo") {
        val id = reqArg<String>("id")

        operator fun invoke(
            id: String,
        ) = bundleOf(this.id set id)
    }

    object TeacherInfo : Screen("scheduleInfoTeacherInfo") {
        val id = reqArg<String>("id")

        operator fun invoke(
            id: String,
        ) = bundleOf(this.id set id)
    }

    object GroupInfo : Screen("scheduleInfoGroupInfo") {
        val id = reqArg<String>("id")

        operator fun invoke(
            id: String,
        ) = bundleOf(this.id set id)
    }

    object PlaceInfo : Screen("scheduleInfoPlaceInfo") {
        val id = reqArg<String>("id")

        operator fun invoke(
            id: String,
        ) = bundleOf(this.id set id)
    }
}
