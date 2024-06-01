package com.edugma.features.schedule.menu.presentation

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


import edugma.shared.core.resources.generated.resources.Res
import edugma.shared.core.resources.generated.resources.*
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.edugma.core.arch.mvi.viewmodel.rememberOnAction
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.molecules.button.EdButton
import com.edugma.core.designSystem.molecules.button.EdButtonSize
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.shapes.bottom
import com.edugma.core.icons.EdIcons
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.ClickListener
import com.edugma.core.utils.viewmodel.getViewModel
import com.edugma.features.schedule.menu.cards.CalendarCard
import com.edugma.features.schedule.menu.cards.ChangeHistoryCard
import com.edugma.features.schedule.menu.cards.FindFreePlaceCard
import com.edugma.features.schedule.menu.cards.LessonsReviewCard
import com.edugma.features.schedule.menu.cards.ScheduleAppWidgetCard
import com.edugma.features.schedule.menu.cards.ScheduleCard
import com.edugma.features.schedule.menu.cards.ScheduleSourcesCard
import com.edugma.features.schedule.menu.model.MenuItem
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
                        text = stringResource(Res.string.sch_schedule),
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
        Text(text = stringResource(Res.string.schedule_menu_need_select_source))
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
                    state = state.main,
                    onScheduleClick = onScheduleClick,
                    modifier = Modifier.weight(menuItem.weight),
                )
            }
            MenuItem.Calendar -> {
                CalendarCard(
                    date = state.date,
                    onScheduleCalendarClick = onScheduleCalendarClick,
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
