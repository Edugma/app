package com.edugma.core.designSystem.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.edugma.core.designSystem.atoms.placeholder.PlaceholderHighlight
import com.edugma.core.designSystem.atoms.placeholder.placeholder
import com.edugma.core.designSystem.atoms.placeholder.shimmer

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
