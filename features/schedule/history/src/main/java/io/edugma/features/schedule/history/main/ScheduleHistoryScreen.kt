package io.edugma.features.schedule.history.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.core.utils.format
import io.edugma.features.base.elements.PrimaryTopAppBar
import io.edugma.features.base.elements.TonalCard
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.getViewModel
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleHistoryScreen(viewModel: ScheduleHistoryViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    ScheduleHistoryContent(
        state = state,
        onBackClick = viewModel::exit,
        onScheduleClick = viewModel::onScheduleClick
    )
}

private val dateFormat = DateTimeFormatter
    .ofPattern("dd MMMM yyyy, hh:mm")

@OptIn(ExperimentalMaterial3Api::class)
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
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    val date = remember(key) {
                        key.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
                    }
                    Text(text = date.format(dateFormat))
                }
            }
        }
    }
}