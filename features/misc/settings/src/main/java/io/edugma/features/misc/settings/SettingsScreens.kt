package io.edugma.features.misc.settings

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.misc.SettingsScreens
import io.edugma.features.misc.settings.appearance.SettingsAppearanceScreen
import io.edugma.features.misc.settings.main.SettingsMainScreen

val settingsScreens = screens {
    addScreen<SettingsScreens.Main> {
        SettingsMainScreen()
    }

    addScreen<SettingsScreens.Appearance> {
        SettingsAppearanceScreen()
    }
}