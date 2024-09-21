package com.edugma.features.app.presentation.main

import co.touchlab.kermit.Logger
import com.edugma.core.api.model.AppState
import com.edugma.core.api.model.ThemeMode
import com.edugma.core.api.repository.AppStateRepository
import com.edugma.core.api.repository.ThemeRepository
import com.edugma.core.api.repository.UrlTemplateRepository
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic2

class MainAppViewModel(
    private val themeRepository: ThemeRepository,
    private val appStateRepository: AppStateRepository,
    private val urlRepository: UrlTemplateRepository
) : FeatureLogic2<MainAppUiState>() {
    override fun initialState(): MainAppUiState {
        return MainAppUiState()
    }

    override fun onCreate() {
        launchCoroutine {
            themeRepository.init()
            themeRepository.getTheme().collect {
                newState {
                    copy(themeMode = it)
                }
            }
        }

        launchCoroutine {
            appStateRepository.state.collect {
                newState {
                    copy(
                        appState = it
                    )
                }
            }
        }

        launchCoroutine {
            val newNodeState = urlRepository.init()
            Logger.d("New nodeState $newNodeState", tag = TAG)
            appStateRepository.newState(
                appStateRepository.state.value.copy(
                    nodeState = newNodeState,
                ),
            )
        }
    }

    companion object {
        private const val TAG = "MainAppViewModel"
    }
}

data class MainAppUiState(
    val themeMode: ThemeMode = ThemeMode.System,
    val appState: AppState = AppState(),
)
