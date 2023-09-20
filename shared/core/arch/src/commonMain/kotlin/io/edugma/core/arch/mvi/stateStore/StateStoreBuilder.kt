package io.edugma.core.arch.mvi.stateStore

import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

class StateStoreBuilder<TState, TAction>(
    private val stateStore: BaseActionViewModel<TState, TAction>,
) {
    private var errorHandler: CoroutineExceptionHandler? = null

    fun errorHandler(errorHandler: CoroutineExceptionHandler) {
        this.errorHandler = errorHandler
    }

    fun build(): BaseActionViewModel<TState, TAction> {
        errorHandler?.let { errorHandler ->
            stateStore._errorHandler = errorHandler
        }
        return stateStore
    }
}
