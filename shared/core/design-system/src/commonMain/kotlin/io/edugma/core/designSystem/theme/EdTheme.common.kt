package io.edugma.core.designSystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import io.edugma.core.designSystem.tokens.colors.CustomColorScheme
import io.edugma.core.designSystem.tokens.colors.dark_Success
import io.edugma.core.designSystem.tokens.colors.dark_SuccessContainer
import io.edugma.core.designSystem.tokens.colors.dark_Warning
import io.edugma.core.designSystem.tokens.colors.dark_WarningContainer
import io.edugma.core.designSystem.tokens.colors.light_Success
import io.edugma.core.designSystem.tokens.colors.light_SuccessContainer
import io.edugma.core.designSystem.tokens.colors.light_Warning
import io.edugma.core.designSystem.tokens.colors.light_WarningContainer
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

    CompositionLocalProvider(LocalCustomColorScheme provides customColors) {
        MaterialTheme(
            colorScheme = colors,
            shapes = EdShapes,
            typography = EdTypography,
            content = content,
        )
    }
}

private val lightCustomColorScheme = CustomColorScheme(
    success = light_Success,
    successContainer = light_SuccessContainer,
    warning = light_Warning,
    warningContainer = light_WarningContainer,
)

private val darkCustomColorScheme = CustomColorScheme(
    success = dark_Success,
    successContainer = dark_SuccessContainer,
    warning = dark_Warning,
    warningContainer = dark_WarningContainer,
)

internal val LocalCustomColorScheme = staticCompositionLocalOf { lightCustomColorScheme }

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
