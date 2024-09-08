package com.edugma.core.navigation.schedule

import com.edugma.navigation.core.destination.Destination
import com.edugma.navigation.core.destination.NoArgDestination
import com.edugma.navigation.core.destination.reqArg
import com.edugma.navigation.core.destination.toBundle
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
