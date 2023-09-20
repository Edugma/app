package io.edugma.core.api.utils

import io.edugma.core.api.model.SnackbarCommand
import io.edugma.core.api.repository.MainSnackbarRepository

fun MainSnackbarRepository.sendError(exception: Throwable) {
    val errorTitle = getErrorTitle(exception)

    send(
        SnackbarCommand.Message(
            title = errorTitle,
            subtitle = exception.message.orEmpty(),
            type = SnackbarCommand.Message.Type.Error,
        ),
    )
}

private fun getErrorTitle(exception: Throwable): String {
    return ""
}
