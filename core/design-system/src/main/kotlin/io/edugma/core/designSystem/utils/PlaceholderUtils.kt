package io.edugma.core.designSystem.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import io.edugma.core.designSystem.atoms.placeholder.PlaceholderHighlight
import io.edugma.core.designSystem.atoms.placeholder.placeholder
import io.edugma.core.designSystem.atoms.placeholder.shimmer

fun Modifier.edPlaceholder(
    visible: Boolean = true,
    // shape: RoundedCornerShape = RoundedCornerShape(4.dp),
): Modifier {
    return this.composed {
        placeholder(
            visible = visible,
            highlight = PlaceholderHighlight.shimmer(),
        )
    }
}
