package io.edugma.core.arch.mvi

import kotlinx.coroutines.flow.StateFlow

interface StateStore<TState> {
    val state: StateFlow<TState>
    fun setState(state: TState)
}

inline fun <TState> StateStore<TState>.updateState(
    newState: TState.() -> TState,
) {
    setState(newState(state.value))
}
