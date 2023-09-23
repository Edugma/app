package io.edugma.features.app.presentation.main

import io.edugma.core.api.model.ThemeMode
import io.edugma.core.api.repository.ThemeRepository
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.utils.viewmodel.launchCoroutine

class MainAppViewModel(
    private val themeRepository: ThemeRepository,
) : BaseViewModel<MainAppUiState>(MainAppUiState()) {
    init {
        launchCoroutine {
            themeRepository.init()
            themeRepository.getTheme().collect {
                newState {
                    copy(themeMode = it)
                }
            }
        }
    }
}

data class MainAppUiState(
    val themeMode: ThemeMode = ThemeMode.System,
)
