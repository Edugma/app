package com.edugma.core.designSystem.tokens.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import edugma.shared.core.design_system.generated.resources.*
import edugma.shared.core.design_system.generated.resources.Res
import org.jetbrains.compose.resources.Font

val openSansFontFamily: FontFamily
    @Composable get() {
        return FontFamily(
            Font(
                resource = Res.font.OpenSans_Light, // W300
                weight = FontWeight.Light,
            ),
            Font(
                resource = Res.font.OpenSans_Regular, // W400
                weight = FontWeight.Normal,
            ),
            Font(
                resource = Res.font.OpenSans_Medium, // W500
                weight = FontWeight.Medium,
            ),
            Font(
                resource = Res.font.OpenSans_SemiBold, // W600
                weight = FontWeight.SemiBold,
            ),
            Font(
                resource = Res.font.OpenSans_Bold, // W700
                weight = FontWeight.Bold,
            ),
            Font(
                resource = Res.font.OpenSans_ExtraBold, // W800
                weight = FontWeight.ExtraBold,
            ),
        )
    }
