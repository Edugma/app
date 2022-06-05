package io.edugma.features.schedule.elements.vertical_schedule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.edugma.domain.base.utils.capitalized
import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonDateTime
import io.edugma.domain.schedule.model.lesson.LessonDisplaySettings
import io.edugma.domain.schedule.model.lesson.LessonTime
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.core.utils.Typed2Listener
import io.edugma.features.base.core.utils.withAlpha
import io.edugma.features.base.elements.SpacerHeight
import io.edugma.features.schedule.elements.lesson.LessonPlace
import io.edugma.features.schedule.elements.lesson.LessonWindow
import io.edugma.features.schedule.elements.lesson.model.ScheduleItem
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalSchedule(
    scheduleDays: List<ScheduleDayUiModel>,
    lessonDisplaySettings: LessonDisplaySettings,
    onLessonClick: Typed2Listener<Lesson, LessonDateTime>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                        is ScheduleItem.LessonByTime -> {
                            LessonPlace(
                                lessonsByTime = item.lesson,
                                lessonDisplaySettings = lessonDisplaySettings,
                                onLessonClick = { lesson, lessonTime ->
                                    onLessonClick(lesson, LessonDateTime(startDate = day.date, endDate = null, time = lessonTime))
                                }
                            )
                        }
                        is ScheduleItem.Window -> {
                            item {
                                LessonWindow(
                                    lessonWindow = item
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
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 10.dp),
            softWrap = true,
            textAlign = TextAlign.Center
        )
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM, yyyy")

@Composable
private fun DayDate(date: LocalDate) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = MaterialTheme3.shapes.small,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            color = MaterialTheme3.colorScheme.background
                .withAlpha(0.8f)
        ) {
            Text(
                date.format(dateFormatter).capitalized(),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                style = MaterialTheme3.typography.bodyMedium
            )
        }
    }
}