package io.edugma.core.designSystem.utils

import androidx.compose.ui.Modifier

inline fun Modifier.ifThen(condition: Boolean, onTrue: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        this.then(onTrue(Modifier))
    } else {
        this
    }
}

inline fun <reified T : Any> Modifier.ifNotNull(
    value: T?,
    onTrue: Modifier.(T) -> Modifier,
): Modifier {
    return if (value != null) {
        this.then(Modifier.onTrue(value))
    } else {
        this
    }
}
