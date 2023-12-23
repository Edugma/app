package io.edugma.core.designSystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import io.edugma.core.designSystem.tokens.colors.CustomColorScheme
import io.edugma.core.designSystem.tokens.colors.LocalCustomColorScheme
import io.edugma.core.designSystem.tokens.colors.LocalPaletteColorScheme
import io.edugma.core.designSystem.tokens.colors.PaletteColorScheme
import io.edugma.core.designSystem.tokens.colors.darkCustomColorScheme
import io.edugma.core.designSystem.tokens.colors.darkPaletteColorScheme
import io.edugma.core.designSystem.tokens.colors.lightCustomColorScheme
import io.edugma.core.designSystem.tokens.colors.lightPaletteColorScheme
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
    val colors = getColorScheme(useDarkTheme, useDynamicColors)

    val customColors = if (useDarkTheme) {
        darkCustomColorScheme
    } else {
        lightCustomColorScheme
    }

    val paletteColors = if (useDarkTheme) {
        darkPaletteColorScheme
    } else {
        lightPaletteColorScheme
    }

    CompositionLocalProvider(
        LocalCustomColorScheme provides customColors,
        LocalPaletteColorScheme provides paletteColors,
    ) {
        MaterialTheme(
            colorScheme = colors,
            shapes = EdShapes,
            typography = EdTypography,
            content = content,
        )
    }
}

@Composable
expect fun getColorScheme(
    useDarkTheme: Boolean,
    useDynamicColors: Boolean,
): ColorScheme

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
        get() = LocalCustomColorScheme.current

    val paletteColorScheme: PaletteColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalPaletteColorScheme.current

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
