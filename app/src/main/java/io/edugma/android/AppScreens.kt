package io.edugma.android

import io.edugma.features.account.accountScreens
import io.edugma.features.base.core.navigation.compose.groupScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.MainScreen
import io.edugma.features.base.navigation.ScheduleScreens
import io.edugma.features.home.homeScreens
import io.edugma.features.misc.menu.miscMenuScreens
import io.edugma.features.nodes.nodesScreens

val appScreens = screens {
    nodesScreens()
    homeScreens()
    groupScreen<MainScreen.Schedule, ScheduleScreens.Menu> {
        io.edugma.features.schedule.screens(this)
        io.edugma.features.schedule.schedule_info.screens(this)
        io.edugma.features.schedule.calendar.screens(this)
        io.edugma.features.schedule.lessons_review.screens(this)
    }
    accountScreens()
    miscMenuScreens()
}