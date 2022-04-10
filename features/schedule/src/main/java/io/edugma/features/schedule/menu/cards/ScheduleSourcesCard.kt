package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.schedule.menu.ScheduleMenuState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleSourcesCard(
    state: ScheduleMenuState.SourceState,
    onScheduleSourceClick: ClickListener
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(90.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onScheduleSourceClick
    ) {
        Box(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = state.selectedSource?.title ?: "Выберите расписание")
        }
    }
}