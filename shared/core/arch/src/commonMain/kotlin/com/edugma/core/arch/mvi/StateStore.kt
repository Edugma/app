package com.edugma.core.arch.mvi

import com.edugma.core.arch.viewmodel.RestrictedApi
import kotlinx.coroutines.flow.StateFlow

interface StateStore<TState> {
    val stateFlow: StateFlow<TState>
    val state: TState

    @RestrictedApi
    fun setState(state: TState)
}

@OptIn(RestrictedApi::class)
inline fun <TState> StateStore<TState>.newState(
    newState: TState.() -> TState,
) {
    setState(newState(stateFlow.value))
}
