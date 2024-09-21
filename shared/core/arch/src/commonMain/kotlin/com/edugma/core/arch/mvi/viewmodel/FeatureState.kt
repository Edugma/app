package com.edugma.core.arch.mvi.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

interface MutableFeatureState<T> : FeatureState<T> {
    fun update(function: (T) -> T)
}

interface FeatureState<T> {
    val value: T
    val flow: Flow<T>
}

fun <T> MutableFeatureState(initialState: T): MutableFeatureState<T> {
    return FeatureStateImpl(initialState = initialState)
}

private class FeatureStateImpl<T>(initialState: T) : MutableFeatureState<T> {
    private val _state = MutableStateFlow(initialState)

    override fun update(function: (T) -> T) {
        // TODO make queue
        _state.update(function)
    }

    override val value: T
        get() = _state.value

    override val flow: Flow<T>
        get() = _state
}

fun <TSource, TDerived> MutableFeatureState<TSource>.derideState(
    transform: (a: TSource) -> TDerived,
    updateSource: TSource.(TDerived) -> TSource,
): MutableFeatureState<TDerived> {
    return DerivedFeatureState(
        sourceState = this,
        transform = transform,
        updateSource = updateSource,
    )
}

internal class DerivedFeatureState<TDerived, TSource>(
    private val sourceState: MutableFeatureState<TSource>,
    private val transform: (TSource) -> TDerived,
    private val updateSource: TSource.(TDerived) -> TSource,
) : MutableFeatureState<TDerived> {

    override fun update(function: (TDerived) -> TDerived) {
        sourceState.update { source ->
            updateSource(source, function(transform(source)))
        }
    }

    override val value: TDerived
        get() = transform(sourceState.value)

    override val flow: Flow<TDerived>
        get() = sourceState.flow.map(transform)
}
