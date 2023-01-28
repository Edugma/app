package io.edugma.features.schedule.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.schedule.menu.cards.*
import org.koin.androidx.compose.getViewModel

@Composable
fun ScheduleMenuScreen(viewModel: ScheduleMenuViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    ScheduleMenuContent(
        state = state,
        onScheduleClick = viewModel::onScheduleClick,
        onLessonsReviewClick = viewModel::onLessonsReviewClick,
        onScheduleCalendarClick = viewModel::onScheduleCalendarClick,
        onScheduleSourceClick = viewModel::onScheduleSourceClick,
        onFreePlaceClick = viewModel::onFreePlaceClick,
        onAppWidgetClick = viewModel::onAppWidgetClick,
        onHistoryClick = viewModel::onHistoryClick,
    )
}

@Composable
fun ScheduleMenuContent(
    state: ScheduleMenuState,
    onScheduleClick: ClickListener,
    onLessonsReviewClick: ClickListener,
    onScheduleCalendarClick: ClickListener,
    onScheduleSourceClick: ClickListener,
    onFreePlaceClick: ClickListener,
    onAppWidgetClick: ClickListener,
    onHistoryClick: ClickListener,
) {
    Column(
        Modifier
            .padding(top = 20.dp, start = 4.dp, end = 4.dp)
            .fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.sch_schedule),
            style = EdTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 16.dp),
        )
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ScheduleSourcesCard(state.source, onScheduleSourceClick)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ScheduleCard(
                state.main,
                onScheduleClick,
            )
            CalendarCard(
                state.date,
                onScheduleCalendarClick,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            LessonsReviewCard(onLessonsReviewClick)
            ChangeHistoryCard(onHistoryClick)
            ScheduleAppWidgetCard(onAppWidgetClick)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            FindFreePlaceCard(onFreePlaceClick)
        }
    }
}
