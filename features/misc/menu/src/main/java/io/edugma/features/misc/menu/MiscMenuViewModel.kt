package io.edugma.features.misc.menu

import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.navigation.misc.SettingsScreens

class MiscMenuViewModel : BaseViewModel<Unit>(Unit) {
    fun onSettingsClick() {
        router.navigateTo(
            SettingsScreens.Main,
        )
    }
}
