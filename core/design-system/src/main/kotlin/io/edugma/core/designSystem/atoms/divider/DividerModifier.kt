package io.edugma.core.designSystem.atoms.divider

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.bottomDivider(
    thickness: Dp = 0.5.dp,
    color: Color = DividerDefaults.color,
): Modifier {

    val density = LocalDensity.current

    val strokeWidthPx = density.run { thickness.toPx() }

    return drawBehind {
        val width = size.width
        val height = size.height - strokeWidthPx / 2

        drawLine(
            color = color,
            start = Offset(x = 0f, y = height),
            end = Offset(x = width, y = height),
            strokeWidth = strokeWidthPx,
        )
    }.padding(bottom = thickness)
}
