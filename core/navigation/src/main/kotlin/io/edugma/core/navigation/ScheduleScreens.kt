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

    object Source : NoArgScreen("scheduleMenu")

    object Calendar : NoArgScreen("scheduleMenu")

    object LessonsReview : NoArgScreen("scheduleMenu")

    object FreePlace : NoArgScreen("scheduleMenu")
}
