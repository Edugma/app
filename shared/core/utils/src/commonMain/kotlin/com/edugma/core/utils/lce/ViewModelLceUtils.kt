package com.edugma.core.utils.lce

import com.edugma.core.api.model.LceUiState
import com.edugma.core.api.utils.LceFailure
import com.edugma.core.api.utils.LceFlow
import com.edugma.core.api.utils.LceSuccess
import com.edugma.core.api.utils.onResult
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import com.edugma.core.arch.mvi.viewmodel.MutableFeatureState

suspend inline fun <T> LceFlow<T>.onDefaultResult(
    viewModel: FeatureLogic<*, *>,
    state: MutableFeatureState<LceUiState>,
    crossinline isContentEmpty: () -> Boolean,
    crossinline onSuccess: (LceSuccess<T>) -> Unit,
    crossinline onFailure: (LceFailure) -> Unit = { },
) {
    this.onResult(
        onSuccess = { successResult ->
            onSuccess(successResult)
            state.update { lceState ->
                if (successResult.isLoading) {
                    lceState.toContent(isContentEmpty())
                } else {
                    lceState.toContent(isContentEmpty())
                        .toFinishLoading()
                }
            }
        },
        onFailure = {
            onFailure(it)
            if (it.isLoading.not()) {
                state.update { lceState ->
                    lceState.toFinalError()
                }
            }
            if (it.isLoading.not()) {
                viewModel.errorHandler?.handleException(it.exceptionOrThrow)
            }
        },
    )
}

inline fun <TState, T> FeatureLogic<TState, *>.launchLce(
    crossinline lceProvider: suspend () -> LceFlow<T>,
    noinline getLceState: TState.() -> LceUiState,
    noinline setLceState: TState.(LceUiState) -> TState,
    isRefreshing: Boolean,
    crossinline isContentEmpty: () -> Boolean,
    crossinline onSuccess: (LceSuccess<T>) -> Unit,
    crossinline onFailure: (LceFailure) -> Unit = { },
) {
    val derivedState = derideState(
        transform = getLceState,
        updateSource = setLceState,
    )
    derivedState.update {
        it.toStartLoading(isRefreshing)
    }
    launchCoroutine(onError = {}) {
        val lceFlow = lceProvider()
        lceFlow.onDefaultResult(
            viewModel = this@launchLce,
            state = derivedState,
            isContentEmpty = isContentEmpty,
            onSuccess = onSuccess,
            onFailure = onFailure,
        )
    }
}
