package com.edugma.core.designSystem.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.edugma.core.designSystem.tokens.colors.EdDarkColors
import com.edugma.core.designSystem.tokens.colors.EdLightColors

@Composable
actual fun getColorScheme(
    useDarkTheme: Boolean,
    useDynamicColors: Boolean,
): ColorScheme {
    return if (useDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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
}
