package io.edugma.features.account.main.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.ContentAlpha
import io.edugma.features.base.core.utils.WithContentAlpha

@Composable
fun PersonalCard(
    info: String?,
    specialization: String?,
    enabled: Boolean,
    onScheduleClick: ClickListener,
) {
    EdCard(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(95.dp)
            .fillMaxWidth(0.6f),
        shape = RoundedCornerShape(16.dp),
        enabled = enabled,
        onClick = onScheduleClick,
    ) {
        Column(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = "О вас")
            Spacer(Modifier.height(4.dp))
            info?.let {
                Text(
                    text = info,
                    style = EdTheme.typography.labelSmall,
                    color = EdTheme.colorScheme.secondary,
                )
            }
            specialization?.let {
                WithContentAlpha(alpha = ContentAlpha.medium) {
                    Text(
                        text = specialization,
                        style = EdTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
