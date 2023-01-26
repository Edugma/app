package io.edugma.features.base.navigation.schedule

import io.edugma.features.base.core.navigation.core.Screen
import kotlinx.datetime.Instant

object ScheduleHistoryScreens {
    object Main : Screen()
    class Changes(
        val first: Instant,
        val second: Instant,
    ) : Screen(
        Changes::first.name to first.serialized(),
        Changes::second.name to second.serialized(),
    )
}
