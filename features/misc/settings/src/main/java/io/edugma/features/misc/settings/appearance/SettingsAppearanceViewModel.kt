package io.edugma.features.misc.settings.appearance

import io.edugma.features.base.core.mvi.BaseViewModel

class SettingsAppearanceViewModel
    : BaseViewModel<SettingsAppearanceState>(SettingsAppearanceState()) {
    fun onNightModeCheckedChange(nightMode: NightMode) {
        mutateState {
            state = state.copy(nightMode = nightMode)
        }
    }
}

data class SettingsAppearanceState(
    val nightMode: NightMode = NightMode.System
)

enum class NightMode {
    Light,
    Dark,
    System
}