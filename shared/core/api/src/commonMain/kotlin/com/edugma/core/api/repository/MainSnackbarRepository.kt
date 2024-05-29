package com.edugma.core.api.repository

import com.edugma.core.api.model.SnackbarCommand

interface MainSnackbarRepository : CommandBus<SnackbarCommand> {
    fun send(message: SnackbarCommand.Message)
    suspend fun sendWithResult(message: SnackbarCommand.Message): Boolean
    fun sendResult(result: SnackbarCommand.Result)
}
