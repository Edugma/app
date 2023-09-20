package io.edugma.features.app.main

import io.edugma.core.api.model.SnackbarCommand
import io.edugma.core.api.repository.MainSnackbarRepository
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.designSystem.utils.CommonImageLoader
import io.edugma.core.utils.viewmodel.launchCoroutine
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance

class MainViewModel(
    val commonImageLoader: CommonImageLoader,
    val iconImageLoader: CommonImageLoader,
    val mainSnackbarRepository: MainSnackbarRepository,
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
    }

    override fun onAction(action: MainAction) {
        when (action) {
            is MainAction.OnSnackbarDismissed -> newState {
                dismissSnackbar(action.snackbar)
            }
        }
    }
}

sealed interface MainAction {
    data class OnSnackbarDismissed(val snackbar: SnackbarCommand.Message) : MainAction
}

data class MainUiState(
    val snackbars: List<SnackbarCommand.Message> = emptyList(),
) {
    fun newSnackbar(snackbar: SnackbarCommand.Message) =
        copy(snackbars = snackbars + snackbar)

    fun dismissSnackbar(snackbar: SnackbarCommand.Message) =
        copy(snackbars = snackbars - snackbar)
}
