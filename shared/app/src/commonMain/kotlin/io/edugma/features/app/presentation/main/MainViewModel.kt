package io.edugma.features.app.presentation.main

import io.edugma.core.api.model.SnackbarCommand
import io.edugma.core.api.repository.MainSnackbarRepository
import io.edugma.core.api.repository.UrlTemplateRepository
import io.edugma.core.api.utils.sendResult
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.designSystem.utils.CommonImageLoader
import io.edugma.core.designSystem.utils.IconImageLoader
import io.edugma.features.misc.other.inAppUpdate.domain.CheckUpdateUseCase
import kotlinx.coroutines.flow.filterIsInstance

class MainViewModel(
    val commonImageLoader: CommonImageLoader,
    val iconImageLoader: IconImageLoader,
    private val mainSnackbarRepository: MainSnackbarRepository,
    private val checkUpdateUseCase: CheckUpdateUseCase,
    private val urlRepository: UrlTemplateRepository,
) : BaseActionViewModel<MainUiState, MainAction>(MainUiState()) {
    init {
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
