package com.edugma.core.api.model

import com.edugma.core.api.utils.UUID
import kotlin.time.Duration

sealed interface SnackbarCommand {
    data class Message(
        val title: String,
        val subtitle: String,
        val action: String,
        val timeToDismiss: Duration?,
        val type: Type,
        val id: String,
    ) : SnackbarCommand {
        constructor(
            title: String,
            subtitle: String,
            action: String = "",
            timeToDismiss: Duration? = null,
            type: Type,
        ) : this(
            title = title,
            subtitle = subtitle,
            action = action,
            timeToDismiss = timeToDismiss,
            type = type,
            id = if (action.isNotEmpty()) UUID.get() else "",
        )

        val needResult: Boolean
            get() = action.isNotEmpty()

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
