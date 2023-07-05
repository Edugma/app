package io.edugma.core.designSystem.molecules.dragger

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.card.EdCardDefaults.cardColors
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.core.designSystem.tokens.shapes.EdShapes

@Composable
fun Dragger(modifier: Modifier = Modifier, width: Dp = 35.dp) {
    EdCard(
        modifier = modifier.height(5.dp).width(width),
        colors = cardColors(Color.Gray, Color.Gray, Color.Gray, Color.Gray),
        elevation = EdElevation.Level0,
        shape = EdShapes.extraLarge,
    ) {}
}
