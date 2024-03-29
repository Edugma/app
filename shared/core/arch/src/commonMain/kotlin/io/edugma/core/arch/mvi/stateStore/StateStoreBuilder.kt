package io.edugma.core.arch.mvi.stateStore

import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.arch.mvi.viewmodel.CombinedErrorHandler

class StateStoreBuilder<TState, TAction>(
    private val stateStore: BaseActionViewModel<TState, TAction>,
) {
    private var errorHandler: CombinedErrorHandler? = null

    fun errorHandler(errorHandler: CombinedErrorHandler) {
        this.errorHandler = errorHandler
    }

    fun build(): BaseActionViewModel<TState, TAction> {
        errorHandler?.let { errorHandler ->
            stateStore._errorHandler = errorHandler
        }
        return stateStore
    }
}
