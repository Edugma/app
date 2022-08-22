package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.core.utils.MediumAlpha
import io.edugma.features.base.elements.InitialAvatar
import io.edugma.features.base.elements.SpacerWidth
import io.edugma.features.base.elements.TonalCard
import io.edugma.features.schedule.menu.ScheduleMenuState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleSourcesCard(
    state: ScheduleMenuState.SourceState,
    onScheduleSourceClick: ClickListener
) {
    TonalCard(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(90.dp)
            .fillMaxWidth(),
        onClick = onScheduleSourceClick,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)) {
            state.selectedSource?.avatarUrl?.let {
                InitialAvatar(url = state.selectedSource.avatarUrl, state.selectedSource.title)
                SpacerWidth(width = 16.dp)
            }
            Column(Modifier.fillMaxWidth()) {
                Text(text = state.selectedSource?.title ?: "Выберите расписание")
                state.selectedSource?.description?.let {
                    MediumAlpha {
                        Text(
                            text = it,
                            style = MaterialTheme3.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}