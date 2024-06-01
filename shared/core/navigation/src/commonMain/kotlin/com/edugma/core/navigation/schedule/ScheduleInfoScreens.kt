package com.edugma.core.navigation.schedule

import com.edugma.navigation.core.destination.Destination
import com.edugma.navigation.core.destination.reqArg
import com.edugma.navigation.core.destination.toBundle
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

object ScheduleInfoScreens {
    object LessonInfo : Destination("scheduleInfoLesson") {
        val eventId = reqArg<String>("eventId")
        val currentDate = reqArg<Int>("currentDate")

        operator fun invoke(
            eventId: String,
            currentDate: LocalDate,
        ) = toBundle {
            destination.eventId set eventId
            destination.currentDate set currentDate.toEpochDays()
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
