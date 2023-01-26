package io.edugma.features.base.navigation

import io.edugma.features.base.core.navigation.core.Screen
import java.time.LocalDate

object ScheduleScreens {

    object Menu : Screen()

    class Main(
        val date: LocalDate? = null,
    ) : Screen(
        Main::date.name to date.serialized(),
    )

    object Source : Screen()

    object Calendar : Screen()

    object LessonsReview : Screen()

    object FreePlace : Screen()
}
