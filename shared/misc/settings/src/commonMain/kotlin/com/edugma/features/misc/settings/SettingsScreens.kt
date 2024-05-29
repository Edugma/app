package com.edugma.features.misc.settings

import com.edugma.core.navigation.misc.SettingsScreens
import com.edugma.features.misc.settings.appearance.SettingsAppearanceScreen
import com.edugma.features.misc.settings.main.SettingsMainScreen
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen

fun NavGraphBuilder.settingsScreens() {
    composeScreen(SettingsScreens.Main) {
        SettingsMainScreen()
    }

    composeScreen(SettingsScreens.Appearance) {
        SettingsAppearanceScreen()
    }
}
