package io.edugma.core.arch.mvi

import kotlinx.coroutines.flow.SharedFlow

interface ActionProducer<T> {
    val action: SharedFlow<T>
    fun sendAction(action: T)
}
