package io.edugma.features.misc.settings.main

import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.misc.SettingsScreens

class SettingsMainViewModel : BaseViewModel<Unit>(Unit) {
    fun onAppearanceClick() {
        miscRouter.navigateTo(SettingsScreens.Appearance())
    }
}
