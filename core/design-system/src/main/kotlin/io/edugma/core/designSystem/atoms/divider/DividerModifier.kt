package io.edugma.core.designSystem.atoms.divider

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.bottomDivider(
    thickness: Dp = 0.5.dp,
    color: Color = Color.Unspecified,
): Modifier = composed {

    val density = LocalDensity.current

    val strokeWidthPx = density.run { thickness.toPx() }
    val lineColor = if (color.isUnspecified) DividerDefaults.color else color

    drawBehind {
        val width = size.width
        val height = size.height - strokeWidthPx / 2

        drawLine(
            color = lineColor,
            start = Offset(x = 0f, y = height),
            end = Offset(x = width, y = height),
            strokeWidth = strokeWidthPx,
        )
    }.padding(bottom = thickness)
}
