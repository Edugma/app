package io.edugma.features.misc.settings.appearance

import io.edugma.core.api.model.ThemeMode
import io.edugma.core.api.repository.ThemeRepository
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import kotlinx.coroutines.flow.collect

class SettingsAppearanceViewModel(
    private val themeRepository: ThemeRepository,
) : BaseActionViewModel<SettingsAppearanceUiState, SettingsAppearanceAction>(
    SettingsAppearanceUiState(),
) {

    init {
        launchCoroutine {
            themeRepository.getTheme().collect {
                newState {
                    copy(themeMode = it)
                }
            }
        }
    }

    override fun onAction(action: SettingsAppearanceAction) {
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
