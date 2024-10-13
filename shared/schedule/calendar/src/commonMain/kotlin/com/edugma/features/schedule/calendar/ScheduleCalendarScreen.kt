package com.edugma.features.schedule.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.edugma.core.arch.mvi.viewmodel.rememberOnAction
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.atoms.spacer.SpacerWidth
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.organism.lceScaffold.EdLceScaffold
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.elevation.EdElevation
import com.edugma.core.designSystem.utils.LocalContentAlpha
import com.edugma.core.designSystem.utils.SecondaryContent
import com.edugma.core.designSystem.utils.ifThen
import com.edugma.core.icons.EdIcons
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.ClickListener
import com.edugma.core.utils.Typed1Listener
import com.edugma.core.utils.ui.isItemFullyVisible
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel
import com.edugma.features.schedule.calendar.model.CalendarDayVO
import com.edugma.features.schedule.calendar.model.CalendarLessonVO
import com.edugma.features.schedule.calendar.model.CalendarWeekVO
import com.edugma.features.schedule.calendar.model.ScheduleCalendarVO
import com.edugma.features.schedule.domain.model.compact.Importance
import edugma.shared.core.icons.generated.resources.ic_fluent_calendar_today_24_regular
import edugma.shared.core.resources.generated.resources.*
import edugma.shared.core.resources.generated.resources.Res
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScheduleCalendarScreen(viewModel: ScheduleCalendarViewModel = getViewModel()) {
    val state by viewModel.collectAsState()

    FeatureScreen(
        statusBarPadding = false,
        navigationBarPadding = false,
    ) {
        ScheduleCalendarContent(
            state = state,
            onBackClick = viewModel::exit,
            onAction = viewModel.rememberOnAction(),
        )
    }
}

@Composable
private fun ScheduleCalendarContent(
    state: ScheduleCalendarUiState,
    onBackClick: ClickListener,
    onAction: (ScheduleCalendarAction) -> Unit,
) {
    var cellCount by remember { mutableStateOf(3) }
//    val q = rememberTransformableState { zoomChange, _, _ ->
//        Logger.d(tag = "ZOOM", zoomChange.toString())
//        cellCount = (cellCount + ((1f - zoomChange) / 0.05f).toInt()).coerceIn(1..6)
//    }

    Box(Modifier.fillMaxSize()) {
        EdLceScaffold(
            lceState = state.lceState,
            onRefresh = { },
            placeholder = { /* TODO */ },
        ) {
            if (state.schedule != null) {
                CalendarThree(
                    schedule = state.schedule,
                    weeksCount = state.settings.weeksCount,
                    currentWeekIndex = state.currentWeekIndex,
                    currentDayOfWeekIndex = state.currentDayOfWeekIndex,
                    onItemClick = {
                        onAction(ScheduleCalendarAction.OnDayClick(it))
                    },
                )
            }
        }

        EdSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
            elevatedAlpha = 0.9f,
        ) {
            EdTopAppBar(
                title = stringResource(Res.string.schedule_cal_calendar),
                onNavigationClick = onBackClick,
                windowInsets = WindowInsets.statusBars,
                colors = EdTopAppBarDefaults.colors(
                    Color.Transparent,
                ),
            )
        }
    }
}

@Composable
private fun BoxScope.CalendarThree(
    schedule: ScheduleCalendarVO,
    weeksCount: Int,
    currentWeekIndex: Int,
    currentDayOfWeekIndex: Int,
    onItemClick: Typed1Listener<LocalDate>,
) {
    val scrollState = rememberLazyListState()
    var wasScrolled by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = currentWeekIndex) {
        if (currentWeekIndex == -1) {
            return@LaunchedEffect
        }
        if (!wasScrolled) {
            scrollState.scrollToItem(currentWeekIndex)
            wasScrolled = true
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = scrollState,
        contentPadding = PaddingValues(
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() +
                EdTopAppBarDefaults.ContainerHeight,
        ),
    ) {
        items(weeksCount) { index ->
            val week = schedule.get(index)
            SpacerHeight(9.dp)
            CalendarWeek(
                week = week,
                currentDayOfWeekIndex = currentDayOfWeekIndex,
                onItemClick = onItemClick,
                isCurrentWeek = index == currentWeekIndex,
            )
        }
    }
    val coroutineScope = rememberCoroutineScope()

    Fab(
        scrollState = scrollState,
        currentWeekIndex = currentWeekIndex,
        onClick = {
            if (currentWeekIndex != -1) {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(currentWeekIndex)
                }
            }
        },
    )
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
private fun LazyListState.isItemVisible(itemIndex: Int): Boolean {
    return remember(this, itemIndex) {
        derivedStateOf { isItemFullyVisible(itemIndex) }
    }.value
}

@Composable
private fun BoxScope.Fab(scrollState: LazyListState, currentWeekIndex: Int, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = !scrollState.isItemVisible(currentWeekIndex),
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .navigationBarsPadding()
            .padding(24.dp)
            .navigationBarsPadding(),
        enter = fadeIn() + slideInVertically { it / 2 } + scaleIn(),
        exit = slideOutVertically { it / 2 } + fadeOut() + scaleOut(),
    ) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            containerColor = EdTheme.colorScheme.primary,
            text = {
                Text(text = "На сегодня")
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

@Composable
private fun CalendarWeek(
    week: CalendarWeekVO,
    isCurrentWeek: Boolean,
    currentDayOfWeekIndex: Int,
    onItemClick: Typed1Listener<LocalDate>,
) {
    EdSurface(
        modifier = Modifier.fillMaxWidth(),
        elevation = EdElevation.Level1,
        shape = RectangleShape,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            WeekNumber(
                weekNumber = week.weekNumber,
                isCurrentWeek = isCurrentWeek,
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
            ) {
                CalendarItem(
                    day = week.weekSchedule[0],
                    isCurrentDay = isCurrentWeek && currentDayOfWeekIndex == 0,
                    isStart = true,
                    isEnd = false,
                    modifier = Modifier.weight(1f),
                    onItemClick = { onItemClick(week.weekSchedule[0].date) },
                )
                CalendarItem(
                    day = week.weekSchedule[1],
                    isCurrentDay = isCurrentWeek && currentDayOfWeekIndex == 1,
                    isStart = false,
                    isEnd = false,
                    modifier = Modifier.weight(1f),
                    onItemClick = { onItemClick(week.weekSchedule[1].date) },
                )
                CalendarItem(
                    day = week.weekSchedule[2],
                    isCurrentDay = isCurrentWeek && currentDayOfWeekIndex == 2,
                    isStart = false,
                    isEnd = true,
                    modifier = Modifier.weight(1f),
                    onItemClick = { onItemClick(week.weekSchedule[2].date) },
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
            ) {
                CalendarItem(
                    day = week.weekSchedule[3],
                    isCurrentDay = isCurrentWeek && currentDayOfWeekIndex == 3,
                    isStart = true,
                    isEnd = false,
                    modifier = Modifier.weight(1f),
                    onItemClick = { onItemClick(week.weekSchedule[3].date) },
                )
                CalendarItem(
                    day = week.weekSchedule[4],
                    isCurrentDay = isCurrentWeek && currentDayOfWeekIndex == 4,
                    isStart = false,
                    isEnd = false,
                    modifier = Modifier.weight(1f),
                    onItemClick = { onItemClick(week.weekSchedule[4].date) },
                )
                CalendarItem(
                    day = week.weekSchedule[5],
                    isCurrentDay = isCurrentWeek && currentDayOfWeekIndex == 5,
                    isStart = false,
                    isEnd = true,
                    modifier = Modifier.weight(1f),
                    onItemClick = { onItemClick(week.weekSchedule[5].date) },
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
            ) {
                CalendarItem(
                    day = week.weekSchedule[6],
                    isCurrentDay = isCurrentWeek && currentDayOfWeekIndex == 6,
                    isStart = true,
                    isEnd = false,
                    modifier = Modifier.weight(1f),
                    onItemClick = { onItemClick(week.weekSchedule[6].date) },
                )
                DayStub(
                    modifier = Modifier.weight(1f),
                )
                DayStub(
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.WeekNumber(
    weekNumber: Int,
    isCurrentWeek: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(vertical = 9.dp, horizontal = 6.dp),
    ) {
        EdSurface(
            modifier = modifier,
            color = if (isCurrentWeek) {
                EdTheme.colorScheme.primaryContainer
            } else {
                Color.Transparent
            },
            shape = EdTheme.shapes.small,
        ) {
            Text(
                text = weekNumber.toString(),
                style = EdTheme.typography.labelMedium,
                color = if (isCurrentWeek) {
                    EdTheme.colorScheme.onPrimaryContainer
                } else {
                    EdTheme.colorScheme.primary
                },
                modifier = Modifier
                    .padding(bottom = 3.dp, top = 1.dp)
                    .ifThen(isCurrentWeek) {
                        padding(start = 4.dp, end = 4.dp)
                    },
                textAlign = TextAlign.Center,
            )
        }
        SpacerWidth(width = 3.dp)
        Text(
            text = "Неделя".uppercase(),
            style = EdTheme.typography.labelMedium,
            color = EdTheme.colorScheme.tertiary.copy(alpha = 0.8f),
            modifier = Modifier.padding(bottom = 3.dp, top = 1.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CalendarItem(
    day: CalendarDayVO,
    isCurrentDay: Boolean,
    isStart: Boolean,
    isEnd: Boolean,
    onItemClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxSize()
            .heightIn(min = 48.dp)
            .clickable { onItemClick() }
            .background(
                color = if (isCurrentDay) {
                    EdTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                } else {
                    Color.Transparent
                },
            )
            .padding(bottom = 8.dp),
    ) {
        EdSurface(
            modifier = Modifier.fillMaxWidth(),
            elevation = EdElevation.Level3,
            color = if (isCurrentDay) {
                EdTheme.colorScheme.tertiaryContainer
            } else {
                EdTheme.colorScheme.surface
            },
        ) {
            EdLabel(
                text = day.dayTitle,
                style = EdTheme.typography.labelSmall,
                modifier = Modifier
                    .fillMaxWidth(),
                color = if (isCurrentDay) {
                    EdTheme.colorScheme.onTertiaryContainer
                } else {
                    EdTheme.colorScheme.secondary
                },
                textAlign = TextAlign.Center,
            )
        }
        if (day.lessons.isNotEmpty()) {
            SpacerHeight(4.dp)
        }
        day.lessons.forEachIndexed { index, lesson ->
            if (index != 0) {
                SpacerHeight(3.dp)
            }
            SecondaryContent {
                Lesson(lesson, isStart, isEnd)
            }
        }
    }
}

@Composable
private fun Lesson(
    lesson: CalendarLessonVO,
    isStart: Boolean,
    isEnd: Boolean,
) {
    val containerColor = if (lesson.importance == Importance.High) {
        EdTheme.colorScheme.error.copy(alpha = 0.8f)
    } else {
        EdTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
    }
    val textColor = if (lesson.importance == Importance.High) {
        EdTheme.colorScheme.onError.copy(alpha = LocalContentAlpha.current)
    } else {
        EdTheme.colorScheme.onSecondaryContainer.copy(alpha = LocalContentAlpha.current)
    }

    Box(
        Modifier
            .height(IntrinsicSize.Min)
            .padding(
                start = when {
                    isStart -> 3.dp
                    !isEnd -> 2.dp
                    else -> 1.5.dp
                },
                end = when {
                    isEnd -> 3.dp
                    !isStart -> 2.dp
                    else -> 1.5.dp
                },
            )
            .fillMaxWidth()
            .background(
                containerColor,
                EdTheme.shapes.extraSmall,
            ),
    ) {
        EdLabel(
            text = lesson.title,
            style = EdTheme.typography.labelSmall,
            modifier = Modifier.padding(
                start = 2.5.dp,
                end = 2.5.dp,
                bottom = 2.dp,
                top = 1.dp,
            ),
            maxLines = 3,
            color = textColor,
        )
    }
}

@Composable
private fun DayStub(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        EdSurface(
            modifier = Modifier.fillMaxWidth(),
            elevation = EdElevation.Level3,
        ) {
            EdLabel(
                text = "",
                style = EdTheme.typography.labelSmall,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
