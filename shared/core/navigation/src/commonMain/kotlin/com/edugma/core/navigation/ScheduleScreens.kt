package com.edugma.core.navigation

import com.edugma.navigation.core.destination.NoArgDestination
import com.edugma.navigation.core.destination.Destination
import com.edugma.navigation.core.destination.optArg
import com.edugma.navigation.core.destination.toBundle
import kotlinx.datetime.LocalDate

object ScheduleScreens {
    object Menu : NoArgDestination("scheduleMenu")

    object Main : Destination("scheduleMain") {
        val date = optArg<Int>("date", 0)

        operator fun invoke(
            date: LocalDate? = null,
        ) = toBundle {
            destination.date set date?.toEpochDays()
        }
    }

    object Source : NoArgDestination("scheduleSource")

    object Calendar : NoArgDestination("scheduleCalendar")

    object LessonsReview : NoArgDestination("scheduleLessonsReview")

    object FreePlace : NoArgDestination("scheduleFreePlace")
}
