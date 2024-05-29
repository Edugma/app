package com.edugma.core.api.utils

import com.edugma.core.api.model.ResponseError
import com.edugma.core.api.model.SnackbarCommand
import com.edugma.core.api.repository.MainSnackbarRepository
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerializationException
import kotlin.time.Duration

fun MainSnackbarRepository.sendError(exception: Throwable) {
    val (errorTitle, errorSubtitle) = getErrorTitle(exception)

    send(
        SnackbarCommand.Message(
            title = errorTitle,
            subtitle = errorSubtitle,
            type = SnackbarCommand.Message.Type.Error,
        ),
    )
}

internal sealed interface KnownException {
    val message: String

    data class NetworkException(
        override val message: String,
        val isDeviceProblem: Boolean,
    ) : KnownException

    data class SerializationException(
        override val message: String,
        val isNetwork: Boolean,
    ) : KnownException

    data class HttpException(
        override val message: String,
        val code: Int,
    ) : KnownException

    data class Unknown(
        override val message: String,
        val exception: Throwable,
    ) : KnownException
}

internal expect fun getKnownException(e: Throwable): KnownException

internal fun getCommonKnownException(e: Throwable): KnownException {
    return when (e) {
        is SerializationException -> KnownException.SerializationException(
            message = e.message.orEmpty(),
            isNetwork = false,
        )
        is ResponseError -> {
            when (e) {
                is ResponseError.HttpError -> KnownException.HttpException(
                    message = HttpStatusCode.fromValue(e.code).toString(),
                    code = e.code,
                )

                is ResponseError.NetworkError -> KnownException.NetworkException(
                    message = e.error.message.orEmpty(),
                    isDeviceProblem = false,
                )

                is ResponseError.SerializationError -> KnownException.SerializationException(
                    message = e.error.message.orEmpty(),
                    isNetwork = true,
                )
                is ResponseError.UnknownResponseError -> {
                    if (e.error != null) {
                        getKnownException(e.error)
                    } else {
                        KnownException.Unknown(
                            message = "",
                            exception = e,
                        )
                    }
                }
            }
        }
        else -> KnownException.Unknown(
            message = e.message.orEmpty(),
            exception = e,
        )
    }
}

private fun getErrorTitle(exception: Throwable): Pair<String, String> {
    val e = getKnownException(exception)
    val (title, subtitle) = when (e) {
        is KnownException.HttpException -> "Ошибка запроса" to "Попробуйте повторить позже"
        is KnownException.NetworkException -> if (e.isDeviceProblem) {
            "Ошибка сети" to "Проверьте доступ к интернету на устройстве"
        } else {
            "Ошибка сети" to "Попробуйте повторить позже"
        }
        is KnownException.SerializationException -> if (e.isNetwork) {
            "Ошибка преобразования" to "Неверный формат ответа сервера"
        } else {
            "Ошибка преобразования" to "Неверный формат сохранённых данных"
        }
        is KnownException.Unknown -> "Неизвестная ошибка" to ""
    }

    return title to subtitle.withDetails(e)
}

private fun String.withDetails(exception: KnownException): String {
    return if (exception.message.isEmpty()) {
        this
    } else {
        "$this\nДетали: ${exception.message}"
    }
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
