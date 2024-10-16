package com.edugma.features.schedule.daily.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edugma.core.api.utils.DateFormat
import com.edugma.core.api.utils.format
import com.edugma.core.arch.mvi.viewmodel.rememberOnAction
import com.edugma.core.designSystem.atoms.loader.EdLoader
import com.edugma.core.designSystem.atoms.loader.EdLoaderSize
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.atoms.spacer.SpacerWidth
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.organism.lceScaffold.EdLceScaffold
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.shapes.bottom
import com.edugma.core.designSystem.tokens.shapes.top
import com.edugma.core.icons.EdIcons
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.ui.bindTo
import com.edugma.core.utils.ui.onPageChanged
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel
import edugma.shared.core.icons.generated.resources.*
import edugma.shared.core.resources.generated.resources.*
import edugma.shared.core.resources.generated.resources.Res
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = getViewModel(),
    date: LocalDate? = null,
) {
    val state by viewModel.collectAsState()

    LaunchedEffect(date) {
        viewModel.onArgs(date)
    }

    FeatureScreen(
        statusBarPadding = false,
        navigationBarPadding = false,
    ) {
        ScheduleContent(
            state = state,
            onAction = viewModel.rememberOnAction(),
        )
    }
}

@Composable
fun ScheduleContent(
    state: ScheduleDailyUiState,
    onAction: (ScheduleDailyAction) -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        if (state.weeks != null) {
            val weekPagerState = rememberPagerState(state.weeksIndex) { state.weeks.size }
            weekPagerState.bindTo(state.weeksIndex)
            weekPagerState.onPageChanged { onAction(ScheduleDailyAction.OnWeeksPosChanged(it)) }

            EdSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = EdTheme.shapes.large.bottom(),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    EdTopAppBar(
                        title = stringResource(Res.string.sch_schedule),
                        subtitle = state.selectedDate.format(DateFormat.FULL_PRETTY),
                        onNavigationClick = { onAction(ScheduleDailyAction.OnBack) },
                        actions = {
                            if (state.lceState.showContentLoader) {
                                EdLoader(
                                    size = EdLoaderSize.medium,
                                )
                                SpacerWidth(16.dp)
                            }
                        },
                        colors = EdTopAppBarDefaults.transparent(),
                        windowInsets = WindowInsets.statusBars,
                    )
                    DaysPager(
                        weeks = state.weeks,
                        dayOfWeekPos = state.dayOfWeekIndex,
                        pagerState = weekPagerState,
                        onDayClick = {
                            onAction(ScheduleDailyAction.OnDayClick(it))
                        },
                        modifier = Modifier.padding(bottom = 3.dp),
                    )
                }
            }
        }
        SpacerHeight(height = 10.dp)
        EdSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = EdTheme.shapes.large.top(),
        ) {
            EdLceScaffold(
                lceState = state.lceState,
                onRefresh = { onAction(ScheduleDailyAction.OnRefresh) },
                pullToRefreshEnabled = false,
                placeholder = { ScheduleDayPlaceholder(4) },
            ) {
                if (state.schedule != null) {
                    val schedulePagerState = rememberPagerState(state.scheduleIndex) {
                        state.schedule.size
                    }
                    schedulePagerState.bindTo(state.scheduleIndex)
                    schedulePagerState.onPageChanged {
                        onAction(ScheduleDailyAction.OnSchedulePosChanged(it))
                    }

                    Box(Modifier.fillMaxSize()) {
                        SchedulePager(
                            scheduleDays = state.schedule,
                            lessonDisplaySettings = state.lessonDisplaySettings,
                            lceState = state.lceState,
                            pagerState = schedulePagerState,
                            onLessonClick = { lesson ->
                                onAction(ScheduleDailyAction.OnLessonClick(lesson))
                            },
                            onRefresh = { onAction(ScheduleDailyAction.OnRefresh) },
                        )

                        Fab(state.showBackToTodayFab) {
                            onAction(ScheduleDailyAction.OnFabClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BoxScope.Fab(isVisible: Boolean, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = isVisible,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(24.dp)
            .navigationBarsPadding(),
        enter = fadeIn() + slideInVertically { it / 2 } + scaleIn(),
        exit = slideOutVertically { it / 2 } + fadeOut() + scaleOut(),
    ) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            containerColor = EdTheme.colorScheme.primary,
            text = {
                Text(text = stringResource(Res.string.sch_to_today))
            },
            icon = {
                Icon(
                    painter = painterResource(EdIcons.ic_fluent_calendar_today_24_regular),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
        )
    }
}
