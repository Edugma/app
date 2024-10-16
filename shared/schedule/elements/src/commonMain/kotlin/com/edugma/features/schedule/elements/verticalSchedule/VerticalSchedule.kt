package com.edugma.features.schedule.elements.verticalSchedule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.edugma.core.api.utils.DateFormat
import com.edugma.core.api.utils.capitalized
import com.edugma.core.api.utils.format
import com.edugma.core.api.utils.nowLocalDate
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.utils.withAlpha
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel
import com.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import com.edugma.features.schedule.domain.model.lesson.LessonEvent
import com.edugma.features.schedule.domain.model.source.ScheduleSource
import com.edugma.features.schedule.elements.lesson.LessonPlace
import com.edugma.features.schedule.elements.lesson.LessonWindow
import com.edugma.features.schedule.elements.lesson.model.ScheduleEventUiModel
import com.edugma.features.schedule.elements.model.ScheduleDayUiModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

@Composable
fun VerticalScheduleComponent(
    viewModel: VerticalScheduleViewModel = getViewModel(),
    scheduleSource: ScheduleSource,
) {
    val state by viewModel.collectAsState()

    LaunchedEffect(scheduleSource) {
        viewModel.setScheduleSource(scheduleSource)
    }

    state.schedule?.let {
        VerticalSchedule(
            scheduleDays = state.schedule!!,
            lessonDisplaySettings = state.lessonDisplaySettings,
            currentDayIndex = state.currentDayIndex,
            onLessonClick = viewModel::onLessonClick,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalSchedule(
    scheduleDays: List<ScheduleDayUiModel>,
    lessonDisplaySettings: LessonDisplaySettings,
    currentDayIndex: Int,
    onLessonClick: (LessonEvent) -> Unit,
) {
    val scrollState = rememberLazyListState()

    LaunchedEffect(scheduleDays, currentDayIndex) {
        scrollState.scrollToItem(currentDayIndex)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = scrollState,
    ) {
        scheduleDays.forEach { day ->
            stickyHeader {
                DayDate(date = day.date)
            }
            if (day.lessons.isEmpty()) {
                item {
                    EmptyDay()
                }
            } else {
                day.lessons.forEach { item ->
                    when (item) {
                        is ScheduleEventUiModel.Lesson -> {
                            LessonPlace(
                                lessonEvent = item.lesson,
                                lessonDisplaySettings = lessonDisplaySettings,
                                onLessonClick = onLessonClick,
                            )
                        }
                        is ScheduleEventUiModel.Window -> {
                            item {
                                LessonWindow(
                                    lessonWindow = item,
                                )
                            }
                        }
                    }
                }
            }
        }
        item {
            SpacerHeight(24.dp)
        }
    }
}

@Composable
private fun EmptyDay() {
    Box {
        Text(
            text = "Нет занятий",
            style = EdTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 10.dp),
            softWrap = true,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun DayDate(date: LocalDate) {
    val now = Clock.System.nowLocalDate()

    val dateFormatted = remember(date, now) {
        if (date.year == now.year) {
            date.format(DateFormat.WEEK_DAY_MONTH).capitalized()
        } else {
            date.format(DateFormat.WEEK_DAY_MONTH_YEAR).capitalized()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Surface(
            shape = EdTheme.shapes.small,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            color = EdTheme.colorScheme.background
                .withAlpha(0.8f),
        ) {
            Text(
                dateFormatted,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                style = EdTheme.typography.bodyMedium,
            )
        }
    }
}
