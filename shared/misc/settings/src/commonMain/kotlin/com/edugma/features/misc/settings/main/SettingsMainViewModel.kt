package com.edugma.features.misc.settings.main

import com.edugma.core.arch.mvi.viewmodel.FeatureLogic2
import com.edugma.core.navigation.misc.SettingsScreens

class SettingsMainViewModel : FeatureLogic2<Unit>() {
    override fun initialState() {
        return Unit
    }
    fun onAppearanceClick() {
        miscRouter.navigateTo(SettingsScreens.Appearance())
    }

    fun exit() {
        miscRouter.back()
    }
}
