package com.edugma.core.arch.mvi.viewmodel

import com.edugma.core.api.utils.IO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class FeatureStoreImpl<TState, TAction>(
    override val logic: FeatureLogic<TState, TAction>,
    state: MutableFeatureState<TState>,
    private val errorHandler: CombinedErrorHandler?,
) : FeatureStore<TState, TAction> {

    private val _state = state
    override val state: FeatureState<TState> = _state

    constructor(
        logic: FeatureLogic<TState, TAction>,
        initialState: TState,
        errorHandler: CombinedErrorHandler? = null,
    ) : this(
        logic = logic,
        state = MutableFeatureState(initialState),
        errorHandler = errorHandler,
    )

    override fun create() {
        logic.init(
            state = _state,
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
            errorHandler = errorHandler,
        )
        logic.create()
    }

    override fun destroy() {
        logic.destroy()
    }

    override fun onAction(action: TAction) {
        logic.processAction(action)
    }
}

interface FeatureStore<TState, TAction> {
    val logic: FeatureLogic<TState, TAction>
    fun onAction(action: TAction)
    val state: FeatureState<TState>
    fun create()
    fun destroy()
}

fun <TState, TAction> buildFeatureStore(
    logic: FeatureLogic<TState, TAction>,
    errorHandler: CombinedErrorHandler? = null,
): FeatureStore<TState, TAction> {
    val initialState = logic.initialState()

    return FeatureStoreImpl(
        logic = logic,
        initialState = initialState,
        errorHandler = errorHandler,
    )
}
