package io.edugma.core.designSystem.organism.actionCard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.theme.EdTheme

@Composable
fun EdActionCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String = "",
    width: EdActionCardWidth = EdActionCardWidth.small,
    content: @Composable BoxScope.() -> Unit,
) {
    EdCard(
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(100.dp)
            .width(width.width),
        onClick = onClick,
    ) {
        Column(
            Modifier
                .padding(horizontal = 8.dp, vertical = 9.dp)
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                content()
            }
            EdLabel(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                style = EdTheme.typography.labelMedium,
                maxLines = 2,
            )
            EdLabel(
                text = subtitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                style = EdTheme.typography.labelSmall,
                color = EdTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            )
        }
    }
}
