package io.edugma.features.misc.menu

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.groupScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.MainScreen
import io.edugma.features.base.navigation.misc.MiscMenuScreens
import io.edugma.features.misc.settings.settingsScreens

val miscMenuScreens = screens {
    groupScreen<MainScreen.Misc, MiscMenuScreens.Menu> {
        addScreen<MiscMenuScreens.Menu> { MiscMenuScreen() }
        settingsScreens()
    }
}
