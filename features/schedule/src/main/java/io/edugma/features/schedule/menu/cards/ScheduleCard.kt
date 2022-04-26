package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.ContentAlpha
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.core.utils.WithContentAlpha
import io.edugma.features.base.elements.TonalCard
import io.edugma.features.schedule.menu.ScheduleMenuState
import io.edugma.features.schedule.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCard(
    state: ScheduleMenuState.MainState,
    onScheduleClick: ClickListener
) {
    TonalCard(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(150.dp)
            .fillMaxWidth(0.6f),
        onClick = onScheduleClick,
    ) {
        Column(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = stringResource(R.string.sch_schedule))
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.sch_now).uppercase(),
                style = MaterialTheme3.typography.labelSmall,
                color = MaterialTheme3.colorScheme.secondary
            )
            WithContentAlpha(alpha = ContentAlpha.medium) {
                Text(
                    text = "Информационные системы и технологии",
                    style = MaterialTheme3.typography.bodySmall
                )
            }
        }
    }
}