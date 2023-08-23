package io.edugma.core.arch.mvi.impl

import io.edugma.core.arch.mvi.StateStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SimpleStateStore<TState>(initialState: TState) : StateStore<TState> {
    private val _stateFlow = MutableStateFlow(initialState)
    override val stateFlow = _stateFlow.asStateFlow()

    override val state: TState
        get() = _stateFlow.value

    override fun setState(state: TState) {
        _stateFlow.value = state
    }
}
