package io.edugma.core.designSystem.atoms.loader

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import io.edugma.core.designSystem.theme.EdTheme

@Immutable
data class EdLoaderStyle(
    val color: Color,
) {
    companion object {
        val primary
            @Composable
            @ReadOnlyComposable
            get() = EdLoaderStyle(
                color = EdTheme.colorScheme.primary,
            )

        val secondary
            @Composable
            @ReadOnlyComposable
            get() = EdLoaderStyle(
                color = EdTheme.colorScheme.secondary,
            )

        val content
            @Composable
            @ReadOnlyComposable
            get() = EdLoaderStyle(
                color = LocalContentColor.current,
            )
    }
}
