package io.edugma.core.api.repository

import io.edugma.core.api.model.SnackbarCommand

interface MainSnackbarRepository : CommandBus<SnackbarCommand> {
    fun send(message: SnackbarCommand.Message)
    suspend fun sendWithResult(message: SnackbarCommand.Message): Boolean
    fun sendResult(result: SnackbarCommand.Result)
}
