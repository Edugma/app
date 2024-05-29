package com.edugma.features.misc.settings.main

import com.edugma.core.arch.mvi.viewmodel.BaseViewModel
import com.edugma.core.navigation.misc.SettingsScreens

class SettingsMainViewModel : BaseViewModel<Unit>(Unit) {
    fun onAppearanceClick() {
        miscRouter.navigateTo(SettingsScreens.Appearance())
    }

    fun exit() {
        miscRouter.back()
    }
}
