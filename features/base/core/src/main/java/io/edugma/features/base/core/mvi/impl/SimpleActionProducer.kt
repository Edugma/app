package io.edugma.features.base.core.mvi.impl

import io.edugma.features.base.core.mvi.ActionProducer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SimpleActionProducer<T> : ActionProducer<T> {
    private val scope = CoroutineScope(Dispatchers.Main.immediate + Job())

    private val _action = MutableSharedFlow<T>(replay = 0)
    override val action = _action.asSharedFlow()

    override fun sendAction(action: T) {
        scope.launch {
            _action.emit(action)
        }
    }
}