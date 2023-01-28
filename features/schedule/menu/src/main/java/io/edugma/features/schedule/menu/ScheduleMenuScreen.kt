package io.edugma.features.schedule.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
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
        SpacerHeight(height = 14.dp)
        ScheduleSourcesCard(state.source, onScheduleSourceClick)
        SpacerHeight(height = 10.dp)
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ScheduleCard(
                state.main,
                onScheduleClick,
                modifier = Modifier.weight(2f),
            )
            CalendarCard(
                state.date,
                onScheduleCalendarClick,
                modifier = Modifier.weight(1f),
            )
        }
        SpacerHeight(height = 8.dp)
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LessonsReviewCard(
                onLessonsReviewClick = onLessonsReviewClick,
                modifier = Modifier.weight(1f),
            )
            ChangeHistoryCard(
                onLessonsReviewClick = onHistoryClick,
                modifier = Modifier.weight(1f),
            )
            ScheduleAppWidgetCard(
                onScheduleWidget = onAppWidgetClick,
                modifier = Modifier.weight(1f),
            )
        }
        SpacerHeight(height = 8.dp)
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FindFreePlaceCard(onFreePlaceClick)
        }
    }
}
