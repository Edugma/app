package io.edugma.core.api.utils

import io.edugma.core.api.model.SnackbarCommand
import io.edugma.core.api.repository.MainSnackbarRepository
import kotlin.time.Duration

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

fun MainSnackbarRepository.sendInfo(
    title: String,
    subtitle: String,
) {
    send(
        SnackbarCommand.Message(
            title = title,
            subtitle = subtitle,
            type = SnackbarCommand.Message.Type.Info,
        ),
    )
}

fun MainSnackbarRepository.sendWarning(
    title: String,
    subtitle: String,
) {
    send(
        SnackbarCommand.Message(
            title = title,
            subtitle = subtitle,
            type = SnackbarCommand.Message.Type.Warning,
        ),
    )
}

suspend fun MainSnackbarRepository.sendWarningWithResult(
    title: String,
    subtitle: String,
    action: String,
    timeToDismiss: Duration? = null,
): Boolean {
    return sendWithResult(
        SnackbarCommand.Message(
            title = title,
            subtitle = subtitle,
            action = action,
            type = SnackbarCommand.Message.Type.Warning,
            timeToDismiss = timeToDismiss,
        ),
    )
}

fun MainSnackbarRepository.sendResult(message: SnackbarCommand.Message, result: Boolean) {
    sendResult(
        SnackbarCommand.Result(
            id = message.id,
            result = result,
        ),
    )
}
