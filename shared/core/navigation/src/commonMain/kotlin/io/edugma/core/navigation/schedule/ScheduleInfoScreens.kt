package io.edugma.core.navigation.schedule

import io.edugma.navigation.core.screen.Destination
import io.edugma.navigation.core.screen.reqArg
import io.edugma.navigation.core.screen.toBundle

object ScheduleInfoScreens {
    object LessonInfo : Destination("scheduleInfoLesson") {
        val lessonInfo = reqArg<String>("lessonInfo")

        operator fun invoke(
            lessonInfo: String,
        ) = toBundle {
            destination.lessonInfo set lessonInfo
        }
    }

    object SubjectInfo : Destination("scheduleInfoSubjectInfo") {
        val id = reqArg<String>("id")

        operator fun invoke(
            id: String,
        ) = toBundle {
            destination.id set id
        }
    }

    object TeacherInfo : Destination("scheduleInfoTeacherInfo") {
        val id = reqArg<String>("id")

        operator fun invoke(
            id: String,
        ) = toBundle {
            destination.id set id
        }
    }

    object GroupInfo : Destination("scheduleInfoGroupInfo") {
        val id = reqArg<String>("id")

        operator fun invoke(
            id: String,
        ) = toBundle {
            destination.id set id
        }
    }

    object PlaceInfo : Destination("scheduleInfoPlaceInfo") {
        val id = reqArg<String>("id")

        operator fun invoke(
            id: String,
        ) = toBundle {
            destination.id set id
        }
    }
}
