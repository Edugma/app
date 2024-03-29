package io.edugma.features.misc.menu

import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.misc.SettingsScreens

class MiscMenuViewModel : BaseViewModel<Unit>(Unit) {
    fun onSettingsClick() {
        router.navigateTo(
            SettingsScreens.Main(),
        )
    }
}
