package com.edugma.core.designSystem.tokens.typography

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * For other font
 * lineHeight = 1.2 for heading and 1.4 for body
 * letterSpacing = -0.01 for heading and 0 for body
 */
@Composable
private inline fun headingTextStyle(
    fontWeight: FontWeight,
    fontSize: TextUnit,
    lineHeight: TextUnit = fontSize * 1.2,
    letterSpacing: TextUnit = fontSize * -0.01,
): TextStyle {
    return TextStyle(
        fontFamily = openSansFontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize,
        lineHeight = lineHeight,
        letterSpacing = letterSpacing,
    )
}

@Composable
private inline fun bodyTextStyle(
    fontWeight: FontWeight,
    fontSize: TextUnit,
    lineHeight: TextUnit = fontSize * 1.4,
    letterSpacing: TextUnit = fontSize * 0.005,
): TextStyle {
    return TextStyle(
        fontFamily = openSansFontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize,
        lineHeight = lineHeight,
        letterSpacing = letterSpacing,
    )
}

@Composable
private inline fun labelTextStyle(
    fontWeight: FontWeight,
    fontSize: TextUnit,
    lineHeight: TextUnit = fontSize * 1.4,
    letterSpacing: TextUnit = fontSize * 0.04,
): TextStyle {
    return TextStyle(
        fontFamily = openSansFontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize,
        lineHeight = lineHeight,
        letterSpacing = letterSpacing,
    )
}

val EdTypography
    @Composable get() = Typography(
        displayLarge = headingTextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 57.sp,
        ),
        displayMedium = headingTextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 45.sp,
        ),
        displaySmall = headingTextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
        ),
        headlineLarge = headingTextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp,
        ),
        headlineMedium = headingTextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 28.sp,
        ),
        headlineSmall = headingTextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
        ),
        titleLarge = bodyTextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
        ),
        titleMedium = bodyTextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        ),
        titleSmall = bodyTextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
        ),
        bodyLarge = bodyTextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
        ),
        bodyMedium = bodyTextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        ),
        bodySmall = bodyTextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        ),
        labelLarge = labelTextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        ),
        labelMedium = labelTextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.5.sp,
        ),
        labelSmall = labelTextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 9.5.sp,
        ),
    )
