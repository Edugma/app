package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.edugma.core.designSystem.organism.accountSelector.EdAccountSelector
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.schedule.menu.ScheduleMenuState

@Composable
fun ScheduleSourcesCard(
    state: ScheduleMenuState.SourceState,
    onScheduleSourceClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    if (state.accountSelectorVO == null) {
        Text(text = "Выберите расписание")
    } else {
        EdAccountSelector(
            state = state.accountSelectorVO,
            onClick = onScheduleSourceClick,
        )
    }
}
