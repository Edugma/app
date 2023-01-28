package io.edugma.core.designSystem.tokens.shapes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val EdShapes = Shapes(
    extraSmall = EdShapeTokens.CornerExtraSmall,
    small = EdShapeTokens.CornerSmall,
    medium = EdShapeTokens.CornerMedium,
    large = EdShapeTokens.CornerLarge,
    extraLarge = EdShapeTokens.CornerExtraLarge,
)

object EdShapeTokens {
    val CornerExtraSmall = RoundedCornerShape(6.0.dp)
    val CornerSmall = RoundedCornerShape(10.0.dp)
    val CornerMedium = RoundedCornerShape(16.0.dp)
    val CornerLarge = RoundedCornerShape(24.0.dp)
    val CornerExtraLarge = RoundedCornerShape(28.0.dp)

    val CornerExtraSmallRadius = 6.0.dp
    val CornerSmallRadius = 10.0.dp
    val CornerMediumRadius = 16.0.dp
    val CornerLargeRadius = 24.0.dp
    val CornerExtraLargeRadius = 28.0.dp
}
