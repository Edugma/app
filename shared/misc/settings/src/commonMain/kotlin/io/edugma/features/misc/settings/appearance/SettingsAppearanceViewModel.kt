package io.edugma.features.misc.settings.appearance

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel

class SettingsAppearanceViewModel :
    BaseViewModel<SettingsAppearanceState>(SettingsAppearanceState()) {
    fun onNightModeCheckedChange(nightMode: NightMode) {
        newState {
            copy(nightMode = nightMode)
        }
    }
}

data class SettingsAppearanceState(
    val nightMode: NightMode = NightMode.System,
)

enum class NightMode {
    Light,
    Dark,
    System,
}
