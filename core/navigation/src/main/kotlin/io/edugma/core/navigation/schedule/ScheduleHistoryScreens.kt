package io.edugma.core.navigation.schedule

import io.edugma.navigation.core.screen.NoArgScreen
import io.edugma.navigation.core.screen.Screen
import io.edugma.navigation.core.screen.bundleOf
import io.edugma.navigation.core.screen.set
import kotlinx.datetime.Instant

object ScheduleHistoryScreens {
    object Main : NoArgScreen("scheduleHistoryMain")
    object Changes : Screen("scheduleHistoryMain") {

        val first = reqArg<Instant>("first")
        val second = reqArg<Instant>("second")

        operator fun invoke(
            first: Instant,
            second: Instant,
        ) = bundleOf(
            this.first set first,
            this.second set second,
        )
    }
}
