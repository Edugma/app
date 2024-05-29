package com.edugma.features.misc.menu

import com.edugma.core.navigation.MainDestination
import com.edugma.core.navigation.misc.MiscMenuScreens
import com.edugma.features.misc.settings.settingsScreens
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen
import com.edugma.navigation.core.graph.graph

fun NavGraphBuilder.miscMenuScreens() {
    graph(MainDestination.Misc, MiscMenuScreens.Menu) {
        composeScreen(MiscMenuScreens.Menu) { MiscMenuScreen() }
        settingsScreens()
    }
}
