package io.edugma.features.misc.settings.appearance

import io.edugma.core.arch.mvi.updateState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel

class SettingsAppearanceViewModel :
    BaseViewModel<SettingsAppearanceState>(SettingsAppearanceState()) {
    fun onNightModeCheckedChange(nightMode: NightMode) {
        updateState {
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
