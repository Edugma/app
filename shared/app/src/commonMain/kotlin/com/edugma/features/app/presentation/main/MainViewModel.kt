package com.edugma.features.app.presentation.main

import com.edugma.core.api.model.SnackbarCommand
import com.edugma.core.api.repository.MainSnackbarRepository
import com.edugma.core.api.repository.UrlTemplateRepository
import com.edugma.core.api.utils.sendResult
import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import com.edugma.core.designSystem.utils.CommonImageLoader
import com.edugma.core.designSystem.utils.IconImageLoader
import com.edugma.features.misc.other.inAppUpdate.domain.CheckUpdateUseCase
import kotlinx.coroutines.flow.filterIsInstance

class MainViewModel(
    val commonImageLoader: CommonImageLoader,
    val iconImageLoader: IconImageLoader,
    private val mainSnackbarRepository: MainSnackbarRepository,
    private val checkUpdateUseCase: CheckUpdateUseCase,
    private val urlRepository: UrlTemplateRepository,
) : FeatureLogic<MainUiState, MainAction>() {
    override fun initialState(): MainUiState {
        return MainUiState()
    }

    override fun onCreate() {
        launchCoroutine {
            mainSnackbarRepository.messageFlow
                .filterIsInstance<SnackbarCommand.Message>()
                .collect {
                    newState {
                        newSnackbar(it)
                    }
                }
        }

        launchCoroutine {
            checkUpdateUseCase()
        }
    }

    override fun processAction(action: MainAction) {
        when (action) {
            is MainAction.OnSnackbarDismissed -> {
                newState {
                    dismissSnackbar(action.message)
                }
                if (action.message.needResult) {
                    mainSnackbarRepository.sendResult(
                        message = action.message,
                        result = false,
                    )
                }
            }

            is MainAction.OnSnackbarActionClicked -> {
                newState {
                    dismissSnackbar(action.message)
                }
                mainSnackbarRepository.sendResult(
                    message = action.message,
                    result = true,
                )
            }
        }
    }
}

sealed interface MainAction {
    data class OnSnackbarDismissed(val message: SnackbarCommand.Message) : MainAction
    data class OnSnackbarActionClicked(val message: SnackbarCommand.Message) : MainAction
}

data class MainUiState(
    val snackbars: List<SnackbarCommand.Message> = emptyList(),
) {
    fun newSnackbar(snackbar: SnackbarCommand.Message) =
        copy(snackbars = snackbars + snackbar)

    fun dismissSnackbar(snackbar: SnackbarCommand.Message) =
        copy(snackbars = snackbars - snackbar)
}
