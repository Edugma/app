package io.edugma.core.api.model

import io.edugma.core.api.utils.UUID

sealed interface SnackbarCommand {
    data class Message(
        val title: String,
        val subtitle: String,
        val type: Type,
        val id: String,
    ) : SnackbarCommand {
        constructor(
            title: String,
            subtitle: String,
            type: Type,
            needResult: Boolean = false,
        ) : this(
            title = title,
            subtitle = subtitle,
            type = type,
            id = if (needResult) UUID.get() else "",
        )

        enum class Type {
            Info,
            Warning,
            Error,
        }
    }

    data class Result(
        val id: String,
        val result: Boolean,
    ) : SnackbarCommand
}
