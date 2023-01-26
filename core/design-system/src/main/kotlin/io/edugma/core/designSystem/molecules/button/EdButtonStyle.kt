package io.edugma.core.designSystem.molecules.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import io.edugma.core.designSystem.theme.EdTheme

@Immutable
data class EdButtonStyle(
    val containerColor: Color,
    val contentColor: Color,
) {
    companion object {
        val primary
            @Composable
            @ReadOnlyComposable
            get() = EdButtonStyle(
                containerColor = EdTheme.colorScheme.primary,
                contentColor = EdTheme.colorScheme.onPrimary,
            )

        val secondary
            @Composable
            @ReadOnlyComposable
            get() = EdButtonStyle(
                containerColor = EdTheme.colorScheme.secondary,
                contentColor = EdTheme.colorScheme.onSecondary,
            )
    }
}
