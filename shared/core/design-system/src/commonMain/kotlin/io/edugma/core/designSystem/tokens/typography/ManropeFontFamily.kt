package io.edugma.core.designSystem.tokens.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.icerock.moko.resources.compose.asFont
import io.edugma.core.designSystem.resources.MR

val manropeFontFamily
    @Composable get() = FontFamily(
        MR.fonts.manrope.extralight.asFont(FontWeight.ExtraLight)!!, // W200
        MR.fonts.manrope.light.asFont(FontWeight.Light)!!, // W300
        MR.fonts.manrope.regular.asFont(FontWeight.Normal)!!, // W400
        MR.fonts.manrope.medium.asFont(FontWeight.Medium)!!, // W500
        MR.fonts.manrope.semibold.asFont(FontWeight.SemiBold)!!, // W600
        MR.fonts.manrope.bold.asFont(FontWeight.Bold)!!, // W700
        MR.fonts.manrope.extrabold.asFont(FontWeight.ExtraBold)!!, // W800
    )
