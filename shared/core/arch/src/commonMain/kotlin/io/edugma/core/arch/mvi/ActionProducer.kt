package io.edugma.core.arch.mvi

interface ActionProducer<T> {
    fun onAction(action: T)
}
