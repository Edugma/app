package io.edugma.core.designSystem.atoms.loader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class EdLoaderSize(
    val size: Dp,
    val strokeWidth: Dp,
) {

    companion object {
        val extraSmall
            @Composable
            @ReadOnlyComposable
            get() = EdLoaderSize(
                size = 12.dp,
                strokeWidth = 1.5.dp,
            )

        val small
            @Composable
            @ReadOnlyComposable
            get() = EdLoaderSize(
                size = 16.dp,
                strokeWidth = 2.dp,
            )

        val medium
            @Composable
            @ReadOnlyComposable
            get() = EdLoaderSize(
                size = 28.dp,
                strokeWidth = 3.dp,
            )

        val large
            @Composable
            @ReadOnlyComposable
            get() = EdLoaderSize(
                size = 48.dp,
                strokeWidth = 5.dp,
            )
    }
}
