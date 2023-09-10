package io.edugma.core.navigation.core.router.external

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class ExternalRouter {
    private val _commandBuffer = MutableSharedFlow<List<ExternalNavigationCommand>>(
        replay = 0,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val commandBuffer: Flow<List<ExternalNavigationCommand>> = _commandBuffer

    private fun executeCommands(vararg commands: ExternalNavigationCommand) {
        _commandBuffer.tryEmit(commands.toList())
    }

    fun share(text: String) {
        executeCommands(
            ExternalNavigationCommand.Share(
                text = text,
            ),
        )
    }

    fun openUri(uri: String) {
        executeCommands(
            ExternalNavigationCommand.OpenUri(
                uri = uri,
            ),
        )
    }

    fun showMessage(text: String) {
        executeCommands(
            ExternalNavigationCommand.Message(
                text = text,
            ),
        )
    }
}
