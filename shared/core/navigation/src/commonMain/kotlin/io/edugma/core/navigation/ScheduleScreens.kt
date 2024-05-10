package io.edugma.core.navigation

import io.edugma.navigation.core.screen.NoArgDestination
import io.edugma.navigation.core.screen.Destination
import io.edugma.navigation.core.screen.optArg
import io.edugma.navigation.core.screen.toBundle
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
