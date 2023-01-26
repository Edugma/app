package io.edugma.features.base.core.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    extraSmall = ShapeTokens.CornerExtraSmall,
    small = ShapeTokens.CornerSmall,
    medium = ShapeTokens.CornerMedium,
    large = ShapeTokens.CornerLarge,
    extraLarge = ShapeTokens.CornerExtraLarge,
)

internal object ShapeTokens {
    // autocomplete menu, select menu, snackbars, standard menu, and text fields
    val CornerExtraSmall = RoundedCornerShape(8.0.dp)

    // chips
    val CornerSmall = RoundedCornerShape(16.0.dp)

    // cards and small FABs
    val CornerMedium = RoundedCornerShape(24.0.dp)

    // large FABs
    val CornerLarge = RoundedCornerShape(16.0.dp)

    // extended FABs, FABs, and navigation drawers
    val CornerExtraLarge = RoundedCornerShape(28.0.dp)
}
