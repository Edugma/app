package io.edugma.features.misc.menu

import io.edugma.core.navigation.MainScreen
import io.edugma.core.navigation.misc.MiscMenuScreens
import io.edugma.features.misc.settings.settingsScreens
import io.edugma.navigation.core.graph.screenModule

val miscMenuScreens = screenModule {
    groupScreen(MainScreen.Misc, MiscMenuScreens.Menu) {
        screen(MiscMenuScreens.Menu) { MiscMenuScreen() }
        settingsScreens()
    }
}
