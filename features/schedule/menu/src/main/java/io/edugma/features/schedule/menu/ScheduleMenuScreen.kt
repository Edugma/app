package io.edugma.features.schedule.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.button.EdButtonSize
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.schedule.menu.cards.*
import io.edugma.features.schedule.menu.model.MenuItem
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
        onSignOut = viewModel::onSignOut,
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
    onSignOut: ClickListener,
) {
    Column(
        Modifier
            .padding(top = 20.dp, start = 4.dp, end = 4.dp)
            .fillMaxSize(),
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.sch_schedule),
                style = EdTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 16.dp).weight(1f),
            )
            if (state.source.accountSelectorVO != null) {
                IconButton(onClick = onSignOut) {
                    Icon(
                        painter = painterResource(EdIcons.ic_fluent_sign_out_24_regular),
                        contentDescription = null,
                    )
                }
            }
        }
        SpacerHeight(height = 14.dp)
        if (state.source.accountSelectorVO == null) {
            NeedSelectScheduleSource(onScheduleSourceClick)
        } else {
            ScheduleSourcesCard(state.source.accountSelectorVO, onScheduleSourceClick)
        }

        SpacerHeight(height = 10.dp)
        state.menuItems.forEach { menuRow ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MenuCard(
                    menuItems = menuRow,
                    state = state,
                    onScheduleClick = onScheduleClick,
                    onLessonsReviewClick = onLessonsReviewClick,
                    onScheduleCalendarClick = onScheduleCalendarClick,
                    onFreePlaceClick = onFreePlaceClick,
                    onAppWidgetClick = onAppWidgetClick,
                    onHistoryClick = onHistoryClick,
                )
            }
            SpacerHeight(height = 8.dp)
        }
    }
}

@Composable
private fun NeedSelectScheduleSource(
    onClick: ClickListener,
) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        Text(text = stringResource(R.string.schedule_menu_need_select_source))
        SpacerHeight(height = 10.dp)
        EdButton(
            text = "Выбрать расписание",
            onClick = onClick,
            size = EdButtonSize.small,
        )
    }
}

@Composable
private fun RowScope.MenuCard(
    menuItems: List<MenuItem>,
    state: ScheduleMenuState,
    onScheduleClick: ClickListener,
    onLessonsReviewClick: ClickListener,
    onScheduleCalendarClick: ClickListener,
    onFreePlaceClick: ClickListener,
    onAppWidgetClick: ClickListener,
    onHistoryClick: ClickListener,
) {
    menuItems.forEach { menuItem ->
        when (menuItem) {
            MenuItem.DailySchedule -> {
                ScheduleCard(
                    state.main,
                    onScheduleClick,
                    modifier = Modifier.weight(menuItem.weight),
                )
            }
            MenuItem.Calendar -> {
                CalendarCard(
                    state.date,
                    onScheduleCalendarClick,
                    modifier = Modifier.weight(menuItem.weight),
                )
            }
            MenuItem.LessonsReview -> {
                LessonsReviewCard(
                    onLessonsReviewClick = onLessonsReviewClick,
                    modifier = Modifier.weight(menuItem.weight),
                )
            }
            MenuItem.ChaneHistory -> {
                ChangeHistoryCard(
                    onLessonsReviewClick = onHistoryClick,
                    modifier = Modifier.weight(menuItem.weight),
                )
            }
            MenuItem.AppWidget -> {
                ScheduleAppWidgetCard(
                    onScheduleWidget = onAppWidgetClick,
                    modifier = Modifier.weight(menuItem.weight),
                )
            }
            MenuItem.FindFreePlace -> {
                FindFreePlaceCard(
                    onFreePlaceClick = onFreePlaceClick,
                    modifier = Modifier.weight(menuItem.weight),
                )
            }
            is MenuItem.Empty -> {
                Spacer(modifier = Modifier.weight(menuItem.weight))
            }
        }
    }
}

@Preview
@Composable
internal fun NeedSelectScheduleSourcePreview() {
    EdTheme {
        NeedSelectScheduleSource(
            onClick = { },
        )
    }
}
