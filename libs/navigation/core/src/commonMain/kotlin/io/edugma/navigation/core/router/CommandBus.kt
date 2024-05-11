package io.edugma.navigation.core.router

import kotlinx.coroutines.flow.Flow

interface CommandBus<T> {
    val commands: Flow<T>
    fun onCommand(command: T)
}
