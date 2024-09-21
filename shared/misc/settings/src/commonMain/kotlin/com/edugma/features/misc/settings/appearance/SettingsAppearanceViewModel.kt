package com.edugma.features.misc.settings.appearance

import com.edugma.core.api.model.ThemeMode
import com.edugma.core.api.repository.ThemeRepository
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import kotlinx.coroutines.flow.collect

class SettingsAppearanceViewModel(
    private val themeRepository: ThemeRepository,
) : FeatureLogic<SettingsAppearanceUiState, SettingsAppearanceAction>() {
    override fun initialState(): SettingsAppearanceUiState {
        return SettingsAppearanceUiState()
    }

    override fun onCreate() {
        launchCoroutine {
            themeRepository.getTheme().collect {
                newState {
                    copy(themeMode = it)
                }
            }
        }
    }

    override fun processAction(action: SettingsAppearanceAction) {
        when (action) {
            is SettingsAppearanceAction.OnThemeModeSelected -> {
                newState {
                    copy(themeMode = action.themeMode)
                }
                launchCoroutine {
                    try {
                        themeRepository.setTheme(action.themeMode)
                    } catch (e: Exception) {
                        val a = e
                    }
                }
            }
        }
    }

    fun exit() {
        miscRouter.back()
    }
}

data class SettingsAppearanceUiState(
    val isSystemTheme: Boolean = true,
    val isDarkTheme: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.System,
)

sealed interface SettingsAppearanceAction {
    data class OnThemeModeSelected(val themeMode: ThemeMode) : SettingsAppearanceAction
}
