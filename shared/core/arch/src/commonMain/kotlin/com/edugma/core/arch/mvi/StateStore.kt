package com.edugma.core.arch.mvi

import com.edugma.core.arch.viewmodel.RestrictedApi
import kotlinx.coroutines.flow.StateFlow

interface StateStore<TState> {
    val stateFlow: StateFlow<TState>
    val state: TState

    @RestrictedApi
    fun setState(state: TState)

    @RestrictedApi
    fun compareAndSet(expect: TState, update: TState): Boolean
}

@OptIn(RestrictedApi::class)
inline fun <TState> StateStore<TState>.newState(
    newState: TState.() -> TState,
) {
    while (true) {
        val prevValue = stateFlow.value
        val nextValue = newState(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return
        }
    }
}
