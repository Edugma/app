package io.edugma.features.base.core.mvi

import io.edugma.features.base.core.mvi.impl.SimpleMutator

// Without state and actions
abstract class SimpleViewModel(
) : BaseViewModelFull<Unit, SimpleMutator<Unit>, Nothing>(
    Unit,
    ::SimpleMutator
)


// Only state
abstract class BaseViewModel<TState>(
    initialState: TState
) : BaseViewModelFull<TState, SimpleMutator<TState>, Nothing>(
    initialState,
    ::SimpleMutator
)


// State and actions
abstract class BaseActionViewModel<TState, TAction>(
    initialState: TState
) : BaseViewModelFull<TState, SimpleMutator<TState>, TAction>(
    initialState,
    ::SimpleMutator
)