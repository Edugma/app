package io.edugma.features.misc.settings

import io.edugma.core.navigation.misc.SettingsScreens
import io.edugma.features.misc.settings.appearance.SettingsAppearanceScreen
import io.edugma.features.misc.settings.main.SettingsMainScreen
import io.edugma.navigation.core.graph.screenModule

val settingsScreens = screenModule {
    screen(SettingsScreens.Main) {
        SettingsMainScreen()
    }

    screen(SettingsScreens.Appearance) {
        SettingsAppearanceScreen()
    }
}
