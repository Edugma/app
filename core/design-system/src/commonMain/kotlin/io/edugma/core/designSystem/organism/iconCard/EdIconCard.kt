package io.edugma.core.designSystem.organism.iconCard

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.organism.actionCard.EdActionCard
import io.edugma.core.designSystem.organism.actionCard.EdActionCardWidth

@Composable
fun EdIconCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: Painter? = null,
    iconTint: Color = Color.Unspecified,
    width: EdActionCardWidth = EdActionCardWidth.small,
) {
    EdActionCard(
        title = title,
        onClick = onClick,
        modifier = modifier,
        subtitle = subtitle,
        width = width,
    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}
