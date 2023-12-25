package io.edugma.core.api.repository

import io.edugma.core.api.utils.IO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseCommandBus<T> : CommandBus<T> {
    private val _messageFlow = MutableSharedFlow<T>(
        extraBufferCapacity = Channel.UNLIMITED,
    )

    private val scope = CoroutineScope(Dispatchers.IO)

    override val messageFlow: SharedFlow<T> = _messageFlow.asSharedFlow()

    fun <T> asyncResult(resultAction: suspend CoroutineScope.() -> T): Deferred<T> {
        return scope.async(block = resultAction)
    }

    protected fun sendCommand(command: T) {
        _messageFlow.tryEmit(command)
    }
}
