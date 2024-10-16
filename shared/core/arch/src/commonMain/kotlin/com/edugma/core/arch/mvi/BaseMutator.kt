package com.edugma.core.arch.mvi

abstract class BaseMutator<TState> {
    private var _state: TState? = null
    var state: TState
        get() = _state!!
        set(value) {
            check(!isMutating && _state != null) { "Mutate state only inside mutation scope" }

            val needChange = _state != value
            if (needChange) {
                _state = value
            }
        }

    private var isMutating = false

    protected inline fun <T> set(stateProperty: T, newValue: T, crossinline action: TState.(T) -> TState): Boolean {
        val isChanged = stateProperty != newValue
        if (isChanged) {
            state = state.action(newValue)
        }
        return isChanged
    }

    inline fun Boolean.then(crossinline action: () -> Unit): Boolean {
        if (this) {
            action()
        }
        return this
    }

    val onStateChanged: MutableList<(TState) -> Unit> = mutableListOf()

    fun mutationScope(mutate: () -> Unit) {
        val oldState = state
        isMutating = true
        mutate()
        isMutating = false
        val newState = state
        if (oldState != newState) {
            onStateChanged.forEach { it(newState) }
        }
    }
}
