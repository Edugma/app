package io.edugma.core.designSystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import io.edugma.core.designSystem.tokens.colors.CustomColorScheme
import io.edugma.core.designSystem.tokens.colors.EdDarkColors
import io.edugma.core.designSystem.tokens.colors.EdLightColors
import io.edugma.core.designSystem.tokens.paddings.EdPaddings
import io.edugma.core.designSystem.tokens.shapes.EdShapes
import io.edugma.core.designSystem.tokens.typography.EdTypography
import kotlin.text.Typography

@Composable
fun EdTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColors: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colors = if (useDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (useDarkTheme) {
            dynamicDarkColorScheme(LocalContext.current)
        } else {
            dynamicLightColorScheme(LocalContext.current)
        }
    } else {
        if (useDarkTheme) {
            EdDarkColors
        } else {
            EdLightColors
        }
    }

    MaterialTheme(
        colorScheme = colors,
        shapes = EdShapes,
        typography = EdTypography,
        content = content,
    )
}

object EdTheme {
    /**
     * Retrieves the current [ColorScheme] at the call site's position in the hierarchy.
     */
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val customColorScheme: CustomColorScheme
        @Composable
        @ReadOnlyComposable
        get() = TODO()

    /**
     * Retrieves the current [Typography] at the call site's position in the hierarchy.
     */
    val typography: androidx.compose.material3.Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    /**
     * Retrieves the current [Shapes] at the call site's position in the hierarchy.
     */
    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    /**
     * Retrieves the current [Shapes] at the call site's position in the hierarchy.
     */
    val paddings: EdPaddings
        @Composable
        @ReadOnlyComposable
        get() = EdPaddings
}
