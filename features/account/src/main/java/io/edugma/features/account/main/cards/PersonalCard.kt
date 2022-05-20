package io.edugma.features.account.main.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.ContentAlpha
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.core.utils.WithContentAlpha
import io.edugma.features.base.elements.TonalCard

@Composable
fun PersonalCard(
    info: String?,
    specialization: String?,
    onScheduleClick: ClickListener
) {
    TonalCard(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(95.dp)
            .fillMaxWidth(0.6f),
        shape = RoundedCornerShape(16.dp),
        onClick = onScheduleClick
    ) {
        Column(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = "О вас")
            Spacer(Modifier.height(4.dp))
            info?.let {
                Text(
                    text = info,
                    style = MaterialTheme3.typography.labelSmall,
                    color = MaterialTheme3.colorScheme.secondary
                )
            }
            specialization?.let {
                WithContentAlpha(alpha = ContentAlpha.medium) {
                    Text(
                        text = specialization,
                        style = MaterialTheme3.typography.bodySmall
                    )
                }
            }
        }
    }
}