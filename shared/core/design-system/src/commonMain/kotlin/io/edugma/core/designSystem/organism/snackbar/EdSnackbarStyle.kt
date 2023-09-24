package io.edugma.core.designSystem.organism.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import dev.icerock.moko.resources.ImageResource
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.icons.EdIcons

@Immutable
data class EdSnackbarStyle(
    val contentColor: Color,
    val backgroundColor: Color,
    val iconRes: ImageResource,
) {
    companion object {
        val default: EdSnackbarStyle
            @Composable
            @ReadOnlyComposable
            get() = EdSnackbarStyle(
                contentColor = Color.Unspecified,
                backgroundColor = EdTheme.colorScheme.surfaceVariant,
                iconRes = EdIcons.ic_fluent_info_24_regular,
            )

        val warning: EdSnackbarStyle
            @Composable
            @ReadOnlyComposable
            get() = EdSnackbarStyle(
                contentColor = EdTheme.customColorScheme.warning,
                backgroundColor = EdTheme.customColorScheme.warningContainer,
                iconRes = EdIcons.ic_fluent_warning_24_regular,
            )

        val error: EdSnackbarStyle
            @Composable
            @ReadOnlyComposable
            get() = EdSnackbarStyle(
                contentColor = EdTheme.colorScheme.onErrorContainer,
                backgroundColor = EdTheme.colorScheme.errorContainer,
                iconRes = EdIcons.ic_fluent_error_circle_24_regular,
            )
    }
}
