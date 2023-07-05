package io.edugma.core.arch.mvi.viewmodel

import io.edugma.core.arch.mvi.BaseMutator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.filterAction(actionSelector: () -> T): Flow<Unit> {
    return this.filter { it == actionSelector() }.map { }
}

fun <T, TState, TMutator : BaseMutator<TState>, TAction>
    BaseViewModelFull<TState, TMutator, TAction>
    .rememberActionFlow(actionSelector: () -> T): Flow<Unit> {
    return this.action.filter { it == actionSelector() }.map { }
}

fun <T, Y> Flow<T>.prop(propSelector: T.() -> Y): Flow<Y> {
    return this.map { propSelector(it) }
        .distinctUntilChanged()
}
