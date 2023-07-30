package io.edugma.core.arch.mvi.impl

import io.edugma.core.arch.mvi.StateStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SimpleStateStore<TState>(initialState: TState) : StateStore<TState> {
    private val _state = MutableStateFlow(initialState)
    override val state = _state.asStateFlow()

    override fun setState(state: TState) {
        _state.value = state
    }
}
