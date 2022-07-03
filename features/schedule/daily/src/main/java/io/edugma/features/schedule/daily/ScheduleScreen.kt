package io.edugma.features.schedule.daily

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonDateTime
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.PrimaryTopAppBar
import org.koin.androidx.compose.getViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = getViewModel(),
    date: LocalDate? = null
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initDate(date)
    }

    if (!state.schedule.isNullOrEmpty() || state.isPreloading) {
        ScheduleContent(
            state,
            viewModel::exit,
            viewModel::onFabClick,
            viewModel::onSchedulePosChanged,
            viewModel::onWeeksPosChanged,
            onDayClick = viewModel::onDayClick,
            onLessonClick = viewModel::onLessonClick,
            onRefreshing = viewModel::onRefreshing
        )
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
    onRefreshing: ClickListener
) {
    Box {
        Column(Modifier.fillMaxSize()) {
            val weekPagerState = rememberPagerState(state.weeksPos)
            weekPagerState.bindTo(state.weeksPos)
            weekPagerState.onPageChanged { onWeeksPosChanged(it) }

            PrimaryTopAppBar(
                title = stringResource(R.string.sch_schedule),
                subtitle = state.selectedDate.format(dateTimeFormat),
                showLoading = state.isLoading,
                onBackClick = onBackClick
            )

            DaysPager(
                weeks = state.weeks,
                dayOfWeekPos = state.dayOfWeekPos,
                pagerState = weekPagerState,
                onDayClick = onDayClick
            )

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
                    onRefreshing = onRefreshing
                )
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
            .padding(24.dp),
        enter = fadeIn() + slideInVertically { it / 2 } + scaleIn(),
        exit = slideOutVertically { it / 2 } + fadeOut() + scaleOut(),
    ) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.primary,
            text = {
                Text(text = stringResource(R.string.sch_to_today))
            },
            icon = {
                Icon(
                    painter = painterResource(FluentIcons.ic_fluent_calendar_today_24_regular),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}