package io.edugma.core.designSystem.tokens.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.icerock.moko.resources.FontResource
import dev.icerock.moko.resources.compose.asFont
import io.edugma.core.designSystem.MR

val openSansFontFamily: FontFamily
    @Composable get() {
        return FontFamily(
            MR.fonts.OpenSans.light.asFont(FontWeight.Light) ?: return FontFamily.Default, // W300
            MR.fonts.OpenSans.regular.asFont(FontWeight.Normal) ?: return FontFamily.Default, // W400
            MR.fonts.OpenSans.medium.asFont(FontWeight.Medium) ?: return FontFamily.Default, // W500
            MR.fonts.OpenSans.semiBold.asFont(FontWeight.SemiBold) ?: return FontFamily.Default, // W600
            MR.fonts.OpenSans.bold.asFont(FontWeight.Bold) ?: return FontFamily.Default, // W700
            MR.fonts.OpenSans.extraBold.asFont(FontWeight.ExtraBold) ?: return FontFamily.Default, // W800
        )
    }

