package io.edugma.features.misc.settings.main

import io.edugma.core.navigation.misc.SettingsScreens
import io.edugma.features.base.core.mvi.BaseViewModel

class SettingsMainViewModel : BaseViewModel<Unit>(Unit) {
    fun onAppearanceClick() {
        router.navigateTo(SettingsScreens.Appearance())
    }
}
