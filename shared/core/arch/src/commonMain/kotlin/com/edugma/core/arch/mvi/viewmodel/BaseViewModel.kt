package com.edugma.core.arch.mvi.viewmodel

// Without state and actions
abstract class SimpleViewModel() : BaseActionViewModel<Unit, Nothing>(Unit)

// Only state
abstract class BaseViewModel<TState>(
    initialState: TState,
) : BaseActionViewModel<TState, Nothing>(initialState) {
    override fun processAction(action: Nothing) {
        TODO("Not yet implemented")
    }
}
