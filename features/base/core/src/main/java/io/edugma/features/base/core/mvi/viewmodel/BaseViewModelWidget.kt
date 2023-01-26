package io.edugma.features.base.core.mvi.viewmodel

import io.edugma.features.base.core.mvi.impl.SimpleMutator

// Without state and actions
abstract class SimpleViewModelWidget() : BaseViewModelWidgetFull<Unit, SimpleMutator<Unit>, Nothing>(
    Unit,
    ::SimpleMutator,
)

// Only state
abstract class BaseViewModelWidget<TState>(
    initialState: TState,
) : BaseViewModelWidgetFull<TState, SimpleMutator<TState>, Nothing>(
    initialState,
    ::SimpleMutator,
)

// State and actions
abstract class BaseActionViewModelWidget<TState, TAction>(
    initialState: TState,
) : BaseViewModelWidgetFull<TState, SimpleMutator<TState>, TAction>(
    initialState,
    ::SimpleMutator,
)
