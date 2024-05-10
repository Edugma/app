package io.edugma.core.navigation.schedule

import io.edugma.navigation.core.screen.NoArgDestination
import io.edugma.navigation.core.screen.Destination
import io.edugma.navigation.core.screen.reqArg
import io.edugma.navigation.core.screen.toBundle
import kotlinx.datetime.Instant

object ScheduleHistoryScreens {
    object Main : NoArgDestination("scheduleHistoryMain")
    object Changes : Destination("scheduleHistoryMain") {

        val first = reqArg<Long>("first")
        val second = reqArg<Long>("second")

        operator fun invoke(
            first: Instant,
            second: Instant,
        ) = toBundle {
            destination.first set first.toEpochMilliseconds()
            destination.second set second.toEpochMilliseconds()
        }
    }
}
