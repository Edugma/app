package io.edugma.core.designSystem.tokens.typography

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import io.edugma.core.designSystem.R

val manropeFontFamily = FontFamily(
    Font(R.font.manrope_extralight, FontWeight.ExtraLight), // W200
    Font(R.font.manrope_light, FontWeight.Light), // W300
    Font(R.font.manrope_regular, FontWeight.Normal), // W400
    Font(R.font.manrope_medium, FontWeight.Medium), // W500
    Font(R.font.manrope_semibold, FontWeight.SemiBold), // W600
    Font(R.font.manrope_bold, FontWeight.Bold), // W700
    Font(R.font.manrope_extrabold, FontWeight.ExtraBold), // W800
)
