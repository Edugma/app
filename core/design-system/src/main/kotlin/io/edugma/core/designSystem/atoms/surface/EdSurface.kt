package io.edugma.core.designSystem.atoms.surface

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation

@Composable
fun EdSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = EdTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    elevation: EdElevation = EdElevation.Level1,
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        tonalElevation = elevation.defaultElevation,
        shadowElevation = 0.dp,
        border = border,
        content = content,
    )
}
