package io.edugma.core.designSystem.molecules.iconButton

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
data class EdIconButtonSize(
    val shape: Shape,
    val size: DpSize,
    val loaderSize: EdLoaderSize,
    val spacer: Dp,
) {
    companion object {
        val small
            @Composable
            @ReadOnlyComposable
            get() = EdIconButtonSize(
                shape = EdTheme.shapes.small,
                size = DpSize(
                    width = 32.dp,
                    height = 32.dp,
                ),
                loaderSize = EdLoaderSize.extraSmall,
                spacer = 6.dp,
            )

        val medium
            @Composable
            @ReadOnlyComposable
            get() = EdIconButtonSize(
                shape = EdTheme.shapes.small,
                size = DpSize(
                    width = 44.dp,
                    height = 44.dp,
                ),
                loaderSize = EdLoaderSize.small,
                spacer = 12.dp,
            )

        val large
            @Composable
            @ReadOnlyComposable
            get() = EdIconButtonSize(
                shape = EdTheme.shapes.small,
                size = DpSize(
                    width = 56.dp,
                    height = 56.dp,
                ),
                loaderSize = EdLoaderSize.medium,
                spacer = 16.dp,
            )
    }
}
