package io.edugma.core.designSystem.tokens.shapes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val EdShapes = Shapes(
    extraSmall = ShapeTokens.CornerExtraSmall,
    small = ShapeTokens.CornerSmall,
    medium = ShapeTokens.CornerMedium,
    large = ShapeTokens.CornerLarge,
    extraLarge = ShapeTokens.CornerExtraLarge,
)

internal object ShapeTokens {
    val CornerExtraSmall = RoundedCornerShape(6.0.dp)
    val CornerSmall = RoundedCornerShape(10.0.dp)
    val CornerMedium = RoundedCornerShape(16.0.dp)
    val CornerLarge = RoundedCornerShape(24.0.dp)
    val CornerExtraLarge = RoundedCornerShape(28.0.dp)
}
