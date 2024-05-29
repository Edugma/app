package com.edugma.core.system.repository

import com.edugma.core.api.model.SnackbarCommand
import com.edugma.core.api.repository.BaseCommandBus
import com.edugma.core.api.repository.MainSnackbarRepository
import kotlinx.coroutines.flow.first

class MainSnackbarRepositoryImpl : BaseCommandBus<SnackbarCommand>(), MainSnackbarRepository {

    override fun send(message: SnackbarCommand.Message) {
        sendCommand(message)
    }

    override suspend fun sendWithResult(message: SnackbarCommand.Message): Boolean {
        val resultId = message.id

        val deferredResult = asyncResult {
            (
                messageFlow.first { it is SnackbarCommand.Result && it.id == resultId }
                    as SnackbarCommand.Result
                ).result
        }

        sendCommand(message)

        return deferredResult.await()
    }

    override fun sendResult(result: SnackbarCommand.Result) {
        sendCommand(result)
    }
}
