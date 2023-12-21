package io.edugma.core.designSystem.tokens.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.icerock.moko.resources.compose.asFont
import io.edugma.core.designSystem.MR

val openSansFontFamily
    @Composable get() = FontFamily(
        MR.fonts.OpenSans.light.asFont(FontWeight.Light)!!, // W300
        MR.fonts.OpenSans.regular.asFont(FontWeight.Normal)!!, // W400
        MR.fonts.OpenSans.medium.asFont(FontWeight.Medium)!!, // W500
        MR.fonts.OpenSans.semiBold.asFont(FontWeight.SemiBold)!!, // W600
        MR.fonts.OpenSans.bold.asFont(FontWeight.Bold)!!, // W700
        MR.fonts.OpenSans.extraBold.asFont(FontWeight.ExtraBold)!!, // W800
    )
