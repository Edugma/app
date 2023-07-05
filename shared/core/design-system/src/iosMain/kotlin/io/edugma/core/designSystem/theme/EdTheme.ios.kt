package io.edugma.core.designSystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import io.edugma.core.designSystem.tokens.colors.EdDarkColors
import io.edugma.core.designSystem.tokens.colors.EdLightColors

@Composable
actual fun getColorScheme(
    useDarkTheme: Boolean,
    useDynamicColors: Boolean,
): ColorScheme {
    return if (useDarkTheme) {
        EdDarkColors
    } else {
        EdLightColors
    }
}
