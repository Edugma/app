package io.edugma.core.designSystem.atoms.card

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.theme.EdTheme

object EdCardDefaults {
    internal const val DisabledContainerOpacity = 0.38f
    internal const val DisabledAlpha = 0.38f

    @Composable
    fun cardColors(
        containerColor: Color = EdTheme.colorScheme.surfaceVariant,
        contentColor: Color = contentColorFor(containerColor),
        disabledContainerColor: Color =
            EdTheme.colorScheme.surfaceVariant
                .copy(alpha = DisabledContainerOpacity)
                .compositeOver(
                    MaterialTheme.colorScheme.surfaceColorAtElevation(
                        0.dp,
                    ),
                ),
        disabledContentColor: Color = contentColorFor(containerColor).copy(DisabledAlpha),
    ): EdCardColors = EdCardColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )
}
