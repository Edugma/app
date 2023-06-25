package io.edugma.core.designSystem.organism.actionCard

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class EdActionCardWidth(
    val width: Dp,
) {
    companion object {
        val small
            get() = EdActionCardWidth(
                width = 100.dp,
            )
        val medium
            get() = EdActionCardWidth(
                width = 150.dp,
            )
        val large
            get() = EdActionCardWidth(
                width = 200.dp,
            )
    }
}
