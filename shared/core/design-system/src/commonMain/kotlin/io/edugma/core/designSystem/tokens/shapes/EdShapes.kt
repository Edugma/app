package io.edugma.core.designSystem.tokens.shapes

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
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
    internal val CornerExtraSmallRadius = 4.0.dp
    internal val CornerSmallRadius = 10.0.dp
    internal val CornerMediumRadius = 16.0.dp
    internal val CornerLargeRadius = 24.0.dp
    internal val CornerExtraLargeRadius = 26.0.dp

    val CornerExtraSmall = RoundedCornerShape(CornerExtraSmallRadius)
    val CornerSmall = RoundedCornerShape(CornerSmallRadius)
    val CornerMedium = RoundedCornerShape(CornerMediumRadius)
    val CornerLarge = RoundedCornerShape(CornerLargeRadius)
    val CornerExtraLarge = RoundedCornerShape(CornerExtraLargeRadius)
}

fun CornerBasedShape.top(): CornerBasedShape {
    return copy(
        bottomStart = ZeroCornerSize,
        bottomEnd = ZeroCornerSize,
    )
}

fun CornerBasedShape.bottom(): CornerBasedShape {
    return copy(
        topStart = ZeroCornerSize,
        topEnd = ZeroCornerSize,
    )
}

fun CornerBasedShape.start(): CornerBasedShape {
    return copy(
        topEnd = ZeroCornerSize,
        bottomEnd = ZeroCornerSize,
    )
}

fun CornerBasedShape.end(): CornerBasedShape {
    return copy(
        topStart = ZeroCornerSize,
        bottomStart = ZeroCornerSize,
    )
}
