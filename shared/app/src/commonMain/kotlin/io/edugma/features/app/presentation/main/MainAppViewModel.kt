package io.edugma.features.app.presentation.main

import co.touchlab.kermit.Logger
import io.edugma.core.api.model.AppState
import io.edugma.core.api.model.NodeState
import io.edugma.core.api.model.ThemeMode
import io.edugma.core.api.repository.AppStateRepository
import io.edugma.core.api.repository.ThemeRepository
import io.edugma.core.api.repository.UrlRepository
import io.edugma.core.api.repository.UrlTemplateRepository
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.network.UrlRepositoryImpl

class MainAppViewModel(
    private val themeRepository: ThemeRepository,
    private val appStateRepository: AppStateRepository,
    private val urlRepository: UrlTemplateRepository
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
