package io.edugma.features.schedule.menu.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.arch.mvi.viewmodel.rememberOnAction
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.button.EdButtonSize
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.icons.EdIcons
import io.edugma.core.resources.MR
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.schedule.menu.cards.CalendarCard
import io.edugma.features.schedule.menu.cards.ChangeHistoryCard
import io.edugma.features.schedule.menu.cards.FindFreePlaceCard
import io.edugma.features.schedule.menu.cards.LessonsReviewCard
import io.edugma.features.schedule.menu.cards.ScheduleAppWidgetCard
import io.edugma.features.schedule.menu.cards.ScheduleCard
import io.edugma.features.schedule.menu.cards.ScheduleSourcesCard
import io.edugma.features.schedule.menu.model.MenuItem
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun ScheduleMenuScreen(viewModel: ScheduleMenuViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()

    FeatureScreen(
        statusBarPadding = false,
    ) {
        ScheduleMenuContent(
            state = state,
            onAction = viewModel.rememberOnAction(),
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ScheduleMenuContent(
    state: ScheduleMenuUiState,
    onAction: (ScheduleMenuAction) -> Unit,
) {
    Column(
        Modifier.fillMaxSize(),
    ) {
        EdSurface(
            shape = EdTheme.shapes.large.bottom(),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 20.dp, start = 4.dp, end = 4.dp),
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(MR.strings.sch_schedule),
                        style = EdTheme.typography.headlineMedium,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f),
                    )
                    if (state.source.accountSelectorVO != null) {
                        IconButton(onClick = {
                            onAction(ScheduleMenuAction.OnSignOut)
                        }) {
                            Icon(
                                painter = painterResource(EdIcons.ic_fluent_sign_out_24_filled),
                                contentDescription = null,
                            )
                        }
                    }
                }
                SpacerHeight(height = 14.dp)
                if (state.source.accountSelectorVO == null) {
                    NeedSelectScheduleSource(
                        onClick = {
                            onAction(ScheduleMenuAction.OnScheduleSourceClick)
                        },
                    )
                    SpacerHeight(height = 10.dp)
                } else {
                    ScheduleSourcesCard(
                        accountSelectorVO = state.source.accountSelectorVO,
                        onScheduleSourceClick = {
                            onAction(ScheduleMenuAction.OnScheduleSourceClick)
                        },
                    )
                    SpacerHeight(height = 14.dp)
                }
            }
        }

        SpacerHeight(height = 10.dp)
        state.menuItems.forEach { menuRow ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MenuCard(
                    menuItems = menuRow,
                    state = state,
                    onScheduleClick = {
                        onAction(ScheduleMenuAction.OnScheduleClick)
                    },
                    onLessonsReviewClick = {
                        onAction(ScheduleMenuAction.OnLessonsReviewClick)
                    },
                    onScheduleCalendarClick = {
                        onAction(ScheduleMenuAction.OnScheduleCalendarClick)
                    },
                    onFreePlaceClick = {
                        onAction(ScheduleMenuAction.OnFreePlaceClick)
                    },
                    onAppWidgetClick = {
                        onAction(ScheduleMenuAction.OnAppWidgetClick)
                    },
                    onHistoryClick = {
                        onAction(ScheduleMenuAction.OnHistoryClick)
                    },
                )
            }
            SpacerHeight(height = 8.dp)
        }
    }
}

@Composable
private fun NeedSelectScheduleSource(
    onClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        Text(text = stringResource(MR.strings.schedule_menu_need_select_source))
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
    state: ScheduleMenuUiState,
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

// @Preview
@Composable
internal fun NeedSelectScheduleSourcePreview() {
    EdTheme {
        NeedSelectScheduleSource(
            onClick = { },
        )
    }
}
