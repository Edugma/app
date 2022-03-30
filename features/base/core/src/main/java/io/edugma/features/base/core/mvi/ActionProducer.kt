package io.edugma.features.base.core.mvi

import kotlinx.coroutines.flow.SharedFlow

interface ActionProducer<T> {
    val action: SharedFlow<T>
    fun sendAction(action: T)
}