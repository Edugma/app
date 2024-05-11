package io.edugma.navigation.core.router

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class DefaultCommandBus<T>(
    capacity: Int = Channel.UNLIMITED,
    onBufferOverflow: BufferOverflow = BufferOverflow.DROP_LATEST,
) : CommandBus<T> {

    private val _commands: Channel<T> = Channel(
        capacity = capacity,
        onBufferOverflow = onBufferOverflow,
    )

    override val commands: Flow<T> = _commands.receiveAsFlow()

    override fun onCommand(command: T) {
        _commands.trySend(command)
    }
}
