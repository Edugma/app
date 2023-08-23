package io.edugma.core.arch.mvi

import kotlinx.coroutines.flow.StateFlow

interface StateStore<TState> {
    val stateFlow: StateFlow<TState>
    val state: TState
    fun setState(state: TState)
}

inline fun <TState> StateStore<TState>.newState(
    newState: TState.() -> TState,
) {
    setState(newState(stateFlow.value))
}
