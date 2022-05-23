package io.edugma.features.schedule.history.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.elements.PrimaryTopAppBar
import io.edugma.features.base.elements.SpacerHeight
import org.koin.androidx.compose.getViewModel

@Composable
fun ScheduleHistoryScreen(viewModel: ScheduleHistoryViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    ScheduleHistoryContent(
        state = state,
        onBackClick = viewModel::exit,
    )
}

@Composable
private fun ScheduleHistoryContent(
    state: ScheduleHistoryState,
    onBackClick: ClickListener
) {
    Column(Modifier.fillMaxSize()) {
        PrimaryTopAppBar(
            title = "История изменений",
            onBackClick = onBackClick
        )
        state.history.forEach { (key, value) ->
            Text(text = key.toString())
            Text(text = value.toString())
        }
    }
}