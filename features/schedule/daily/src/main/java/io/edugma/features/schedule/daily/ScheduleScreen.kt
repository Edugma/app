package io.edugma.features.schedule.daily

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import io.edugma.core.designSystem.atoms.loader.EdLoader
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.designSystem.tokens.shapes.top
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.core.utils.Typed2Listener
import io.edugma.features.base.core.utils.bindTo
import io.edugma.features.base.core.utils.onPageChanged
import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonDateTime
import org.koin.androidx.compose.getViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = getViewModel(),
    date: LocalDate? = null,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initDate(date)
    }

    if (!state.schedule.isNullOrEmpty() || state.isPreloading) {
        FeatureScreen(
            statusBarPadding = false,
            navigationBarPadding = false,
        ) {
            ScheduleContent(
                state,
                viewModel::exit,
                viewModel::onFabClick,
                viewModel::onSchedulePosChanged,
                viewModel::onWeeksPosChanged,
                onDayClick = viewModel::onDayClick,
                onLessonClick = viewModel::onLessonClick,
                onRefreshing = viewModel::onRefreshing,
            )
        }
    }
}
private val dateTimeFormat = DateTimeFormatter.ofPattern("d MMMM, yyyy")

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScheduleContent(
    state: ScheduleState,
    onBackClick: ClickListener,
    onFabClick: ClickListener,
    onSchedulePosChanged: Typed1Listener<Int>,
    onWeeksPosChanged: Typed1Listener<Int>,
    onDayClick: Typed1Listener<LocalDate>,
    onLessonClick: Typed2Listener<Lesson, LessonDateTime>,
    onRefreshing: ClickListener,
) {
    Box {
        Column(Modifier.fillMaxSize()) {
            val weekPagerState = rememberPagerState(state.weeksPos)
            weekPagerState.bindTo(state.weeksPos)
            weekPagerState.onPageChanged { onWeeksPosChanged(it) }

            EdSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = EdTheme.shapes.large.bottom(),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    EdTopAppBar(
                        title = stringResource(R.string.sch_schedule),
                        subtitle = state.selectedDate.format(dateTimeFormat),
                        onNavigationClick = onBackClick,
                        actions = {
                            if (state.isLoading) {
                                EdLoader()
                            }
                        },
                        colors = EdTopAppBarDefaults.transparent(),
                        windowInsets = WindowInsets.statusBars,
                    )
                    DaysPager(
                        weeks = state.weeks,
                        dayOfWeekPos = state.dayOfWeekPos,
                        pagerState = weekPagerState,
                        onDayClick = onDayClick,
                        modifier = Modifier.padding(bottom = 3.dp),
                    )
                }
            }
            SpacerHeight(height = 10.dp)
            EdSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = EdTheme.shapes.large.top(),
            ) {
                if (state.isPreloading) {
                    ScheduleDayPlaceHolder()
                } else {
                    val schedulePagerState = rememberPagerState(state.schedulePos)
                    schedulePagerState.bindTo(state.schedulePos)
                    schedulePagerState.onPageChanged { onSchedulePosChanged(it) }

                    SchedulePager(
                        scheduleDays = state.schedule ?: emptyList(),
                        lessonDisplaySettings = state.lessonDisplaySettings,
                        isRefreshing = state.isRefreshing,
                        pagerState = schedulePagerState,
                        onLessonClick = onLessonClick,
                        onRefreshing = onRefreshing,
                    )
                }
            }
        }
        Fab(state.showBackToTodayFab, onFabClick)
    }
}

@OptIn(ExperimentalAnimationApi::class)
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
                Text(text = stringResource(R.string.sch_to_today))
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
