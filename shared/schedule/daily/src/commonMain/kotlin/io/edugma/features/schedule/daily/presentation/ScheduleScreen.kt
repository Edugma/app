package io.edugma.features.schedule.daily.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moriatsushi.insetsx.navigationBarsPadding
import com.moriatsushi.insetsx.statusBars
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.api.utils.format
import io.edugma.core.arch.mvi.viewmodel.rememberOnAction
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.loader.EdLoader
import io.edugma.core.designSystem.atoms.loader.EdLoaderSize
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.designSystem.tokens.shapes.top
import io.edugma.core.icons.EdIcons
import io.edugma.core.resources.MR
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ui.bindTo
import io.edugma.core.utils.ui.onPageChanged
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = getViewModel(),
    date: LocalDate? = null,
) {
    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initDate(date)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleContent(
    state: ScheduleDailyUiState,
    onAction: (ScheduleDailyAction) -> Unit,
) {
    Box {
        Column(Modifier.fillMaxSize()) {
            val weekPagerState = rememberPagerState(state.weeksPos)
            weekPagerState.bindTo(state.weeksPos)
            weekPagerState.onPageChanged { onAction(ScheduleDailyAction.OnWeeksPosChanged(it)) }

            EdSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = EdTheme.shapes.large.bottom(),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    EdTopAppBar(
                        title = stringResource(MR.strings.sch_schedule),
                        subtitle = state.selectedDate.format("d MMMM, yyyy"),
                        onNavigationClick = { onAction(ScheduleDailyAction.OnBack) },
                        actions = {
                            if (state.isLoading && state.schedule != null) {
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
                        dayOfWeekPos = state.dayOfWeekPos,
                        pagerState = weekPagerState,
                        onDayClick = {
                            onAction(ScheduleDailyAction.OnDayClick(it))
                        },
                        modifier = Modifier.padding(bottom = 3.dp),
                    )
                }
            }
            SpacerHeight(height = 10.dp)
            EdSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = EdTheme.shapes.large.top(),
            ) {
                if (state.isLoading && state.schedule == null) {
                    ScheduleDayPlaceholder(4)
                } else {
                    val schedulePagerState = rememberPagerState(state.schedulePos)
                    schedulePagerState.bindTo(state.schedulePos)
                    schedulePagerState.onPageChanged {
                        onAction(ScheduleDailyAction.OnSchedulePosChanged(it))
                    }

                    SchedulePager(
                        scheduleDays = state.schedule ?: emptyList(),
                        lessonDisplaySettings = state.lessonDisplaySettings,
                        isRefreshing = state.isRefreshing,
                        pagerState = schedulePagerState,
                        onLessonClick = { lesson, dateTime ->
                            onAction(ScheduleDailyAction.OnLessonClick(lesson, dateTime))
                        },
                        onRefreshing = {
                            onAction(ScheduleDailyAction.OnRefreshing)
                        },
                    )
                }
            }
        }
        Fab(state.showBackToTodayFab) {
            onAction(ScheduleDailyAction.OnFabClick)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalResourceApi::class)
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
                Text(text = stringResource(MR.strings.sch_to_today))
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
