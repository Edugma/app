package io.edugma.features.schedule.history.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.api.utils.format
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ScheduleHistoryScreen(viewModel: ScheduleHistoryViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    FeatureScreen {
        ScheduleHistoryContent(
            state = state,
            onBackClick = viewModel::exit,
            onScheduleClick = viewModel::onScheduleClick,
        )
    }
}

@Composable
private fun ScheduleHistoryContent(
    state: ScheduleHistoryState,
    onBackClick: ClickListener,
    onScheduleClick: Typed1Listener<Instant>,
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        EdTopAppBar(
            title = "История изменений",
            onNavigationClick = onBackClick,
        )
        state.history.forEach { (key, value) ->
            EdCard(
                onClick = { onScheduleClick(key) },
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
            ) {
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 16.dp)
                        .fillMaxSize(),
                ) {
                    val date = remember(key) {
                        key.toLocalDateTime(TimeZone.currentSystemDefault())
                    }
                    Text(text = date.format("dd MMMM yyyy, hh:mm"))
                }
            }
        }
    }
}
