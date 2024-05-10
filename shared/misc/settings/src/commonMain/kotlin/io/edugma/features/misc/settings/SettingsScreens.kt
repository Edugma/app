package io.edugma.features.misc.settings

import io.edugma.core.navigation.misc.SettingsScreens
import io.edugma.features.misc.settings.appearance.SettingsAppearanceScreen
import io.edugma.features.misc.settings.main.SettingsMainScreen
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.graph.composeScreen

fun NavGraphBuilder.settingsScreens() {
    composeScreen(SettingsScreens.Main) {
        SettingsMainScreen()
    }

    composeScreen(SettingsScreens.Appearance) {
        SettingsAppearanceScreen()
    }
}
