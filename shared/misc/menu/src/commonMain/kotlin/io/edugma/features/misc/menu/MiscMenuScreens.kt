package io.edugma.features.misc.menu

import io.edugma.core.navigation.MainDestination
import io.edugma.core.navigation.misc.MiscMenuScreens
import io.edugma.features.misc.settings.settingsScreens
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.graph.composeScreen
import io.edugma.navigation.core.graph.graph

fun NavGraphBuilder.miscMenuScreens() {
    graph(MainDestination.Misc, MiscMenuScreens.Menu) {
        composeScreen(MiscMenuScreens.Menu) { MiscMenuScreen() }
        settingsScreens()
    }
}
