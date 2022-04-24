package io.edugma.features.schedule.schedule_info

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.schedule.ScheduleInfoScreens
import io.edugma.features.schedule.schedule_info.place_info.PlaceInfoScreen

val screens = screens {
    addScreen<ScheduleInfoScreens.PlaceInfo> {
        PlaceInfoScreen(
            id = getArg(ScheduleInfoScreens.PlaceInfo::id.name)
        )
    }
}