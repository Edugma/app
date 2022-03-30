package io.edugma.android

import io.edugma.features.account.accountScreens
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.home.homeScreens
import io.edugma.features.misc.menu.miscMenuScreens
import io.edugma.features.nodes.nodesScreens
import io.edugma.features.schedule.scheduleScreens

val appScreens = screens {
    nodesScreens()
    homeScreens()
    scheduleScreens()
    accountScreens()
    miscMenuScreens()
}