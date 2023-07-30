package io.edugma.core.arch.mvi.viewmodel

// Without state and actions
abstract class SimpleViewModel() : BaseActionViewModel<Unit, Nothing>(Unit)

// Only state
abstract class BaseViewModel<TState>(
    initialState: TState,
) : BaseActionViewModel<TState, Nothing>(initialState) {
    override fun onAction(action: Nothing) {
        TODO("Not yet implemented")
    }
}
