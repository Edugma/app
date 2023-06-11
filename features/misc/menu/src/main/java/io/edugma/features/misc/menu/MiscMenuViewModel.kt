package io.edugma.features.misc.menu

import io.edugma.core.navigation.misc.SettingsScreens
import io.edugma.features.base.core.mvi.BaseViewModel

class MiscMenuViewModel : BaseViewModel<Unit>(Unit) {
    fun onSettingsClick() {
        router.navigateTo(
            SettingsScreens.Main(),
        )
    }
}
