package com.edugma.core.designSystem.molecules.textButton

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.theme.EdTheme

@Immutable
data class EdTextButtonSize(
    val shape: Shape,
    val contentPadding: PaddingValues,
    val textStyle: TextStyle,
) {
    companion object {
        val small
            @Composable
            @ReadOnlyComposable
            get() = EdTextButtonSize(
                shape = EdTheme.shapes.small,
                contentPadding = PaddingValues(
                    horizontal = 6.dp,
                    vertical = 4.dp,
                ),
                textStyle = EdTheme.typography.bodySmall,
            )

        val medium
            @Composable
            @ReadOnlyComposable
            get() = EdTextButtonSize(
                shape = EdTheme.shapes.small,
                contentPadding = PaddingValues(
                    horizontal = 6.dp,
                    vertical = 4.dp,
                ),
                textStyle = EdTheme.typography.bodyMedium,
            )

        val large
            @Composable
            @ReadOnlyComposable
            get() = EdTextButtonSize(
                shape = EdTheme.shapes.small,
                contentPadding = PaddingValues(
                    horizontal = 6.dp,
                    vertical = 4.dp,
                ),
                textStyle = EdTheme.typography.bodyLarge,
            )
    }
}
