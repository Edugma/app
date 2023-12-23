package io.edugma.core.designSystem.tokens.colors

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
class CustomColorScheme(
    val success: Color,
    val successContainer: Color,
    val warning: Color,
    val warningContainer: Color,
)

internal val lightCustomColorScheme = CustomColorScheme(
    success = light_Success,
    successContainer = light_SuccessContainer,
    warning = light_Warning,
    warningContainer = light_WarningContainer,
)

internal val darkCustomColorScheme = CustomColorScheme(
    success = dark_Success,
    successContainer = dark_SuccessContainer,
    warning = dark_Warning,
    warningContainer = dark_WarningContainer,
)

internal val LocalCustomColorScheme = staticCompositionLocalOf { lightCustomColorScheme }
