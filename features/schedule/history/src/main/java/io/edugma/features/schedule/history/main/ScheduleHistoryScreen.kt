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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.elements.PrimaryTopAppBar
import io.edugma.features.base.elements.TonalCard
import kotlinx.datetime.Instant
import org.koin.androidx.compose.getViewModel

@Composable
fun ScheduleHistoryScreen(viewModel: ScheduleHistoryViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    ScheduleHistoryContent(
        state = state,
        onBackClick = viewModel::exit,
        onScheduleClick = viewModel::onScheduleClick
    )
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
            .verticalScroll(rememberScrollState())
    ) {
        PrimaryTopAppBar(
            title = "История изменений",
            onBackClick = onBackClick
        )
        state.history.forEach { (key, value) ->
            TonalCard(
                onClick = { onScheduleClick(key) },
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    Text(text = key.toString())
                }
            }
        }
    }
}