package io.edugma.core.navigation

import io.edugma.navigation.core.screen.NoArgScreen
import io.edugma.navigation.core.screen.Screen
import io.edugma.navigation.core.screen.bundleOf
import io.edugma.navigation.core.screen.set
import kotlinx.datetime.LocalDate

object ScheduleScreens {
    object Menu : NoArgScreen("scheduleMenu")

    object Main : Screen("scheduleMain") {
        val date = optArg<LocalDate>("date", LocalDate.fromEpochDays(0))

        operator fun invoke(
            date: LocalDate? = null,
        ) = bundleOf(this.date set date)
    }

    object Source : NoArgScreen("scheduleSource")

    object Calendar : NoArgScreen("scheduleCalendar")

    object LessonsReview : NoArgScreen("scheduleLessonsReview")

    object FreePlace : NoArgScreen("scheduleFreePlace")
}
