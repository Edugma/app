package io.edugma.features.misc.settings.main

import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.navigation.misc.SettingsScreens

class SettingsMainViewModel : BaseViewModel<Unit>(Unit) {
    fun onAppearanceClick() {
        router.navigateTo(SettingsScreens.Appearance)
    }
}
