package io.edugma.core.designSystem.molecules.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.loader.EdLoaderSize
import io.edugma.core.designSystem.theme.EdTheme

@Immutable
data class EdButtonSize(
    val shape: Shape,
    val height: Dp,
    val minSize: DpSize,
    val contentPadding: PaddingValues,
    val textStyle: TextStyle,
    val textPadding: PaddingValues,
    val loaderSize: EdLoaderSize,
    val spacer: Dp,
) {
    companion object {
        val small
            @Composable
            @ReadOnlyComposable
            get() = EdButtonSize(
                shape = EdTheme.shapes.extraSmall,
                height = 24.dp,
                minSize = DpSize(
                    width = 38.dp,
                    height = 20.dp,
                ),
                contentPadding = PaddingValues(
                    horizontal = 8.dp,
                    vertical = 0.dp,
                ),
                textStyle = EdTheme.typography.labelSmall,
                textPadding = PaddingValues(bottom = 1.dp),
                loaderSize = EdLoaderSize.extraSmall,
                spacer = 6.dp,
            )

        val medium
            @Composable
            @ReadOnlyComposable
            get() = EdButtonSize(
                shape = EdTheme.shapes.small,
                height = 32.dp,
                minSize = DpSize(
                    width = 48.dp,
                    height = 30.dp,
                ),
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 0.dp,
                ),
                textStyle = EdTheme.typography.labelMedium,
                textPadding = PaddingValues(bottom = 3.dp),
                loaderSize = EdLoaderSize.small,
                spacer = 12.dp,
            )

        val large
            @Composable
            @ReadOnlyComposable
            get() = EdButtonSize(
                shape = EdTheme.shapes.small,
                height = 48.dp,
                minSize = DpSize(
                    width = 58.dp,
                    height = 40.dp,
                ),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 0.dp,
                ),
                textStyle = EdTheme.typography.labelLarge,
                textPadding = PaddingValues(bottom = 5.dp),
                loaderSize = EdLoaderSize.medium,
                spacer = 16.dp,
            )
    }
}
