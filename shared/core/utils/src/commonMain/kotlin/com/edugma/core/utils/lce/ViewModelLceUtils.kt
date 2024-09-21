package com.edugma.core.utils.lce

import co.touchlab.kermit.Logger
import com.edugma.core.api.model.LceUiState
import com.edugma.core.api.utils.LceFailure
import com.edugma.core.api.utils.LceFlow
import com.edugma.core.api.utils.LceSuccess
import com.edugma.core.api.utils.onResult
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic

suspend inline fun <T> LceFlow<T>.onDefaultResult(
    viewModel: FeatureLogic<*, *>,
    crossinline getLce: () -> LceUiState,
    crossinline setLce: (LceUiState) -> Unit,
    crossinline isContentEmpty: () -> Boolean,
    crossinline onSuccess: (LceSuccess<T>) -> Unit,
    crossinline onFailure: (LceFailure) -> Unit = { },
) {
    this.onResult(
        onSuccess = {
            onSuccess(it)
            val lceState = getLce()
            Logger.d("oldLceState=$lceState", tag = "StateStore")
            val newLceState = if (it.isLoading) {
                lceState.toContent(isContentEmpty())
            } else {
                lceState.toContent(isContentEmpty())
                    .toFinishLoading()
            }
            Logger.d("newLceState=$newLceState", tag = "StateStore")
            setLce(newLceState)
        },
        onFailure = {
            onFailure(it)
            if (it.isLoading.not()) {
                val lceState = getLce()
                setLce(lceState.toFinalError())
            }
            if (it.isLoading.not()) {
                viewModel.errorHandler?.handleException(it.exceptionOrThrow)
            }
        },
    )
}

inline fun <T> FeatureLogic<*, *>.launchLce(
    crossinline lceProvider: suspend () -> LceFlow<T>,
    crossinline getLceState: () -> LceUiState,
    crossinline setLceState: (LceUiState) -> Unit,
    isRefreshing: Boolean,
    crossinline isContentEmpty: () -> Boolean,
    crossinline onSuccess: (LceSuccess<T>) -> Unit,
    crossinline onFailure: (LceFailure) -> Unit = { },
) {
    setLceState(getLceState().toStartLoading(isRefreshing))

    launchCoroutine(onError = {}) {
        val lceFlow = lceProvider()
        lceFlow.onDefaultResult(
            viewModel = this@launchLce,
            getLce = getLceState,
            setLce = setLceState,
            isContentEmpty = isContentEmpty,
            onSuccess = onSuccess,
            onFailure = onFailure,
        )
    }
}
