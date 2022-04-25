package io.edugma.features.schedule.main

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import io.edugma.domain.schedule.model.group.Group
import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonDateTime
import io.edugma.domain.schedule.model.lesson.LessonDisplaySettings
import io.edugma.domain.schedule.model.lesson.LessonTime
import io.edugma.domain.schedule.model.lesson_subject.LessonSubject
import io.edugma.domain.schedule.model.place.Place
import io.edugma.domain.schedule.model.place.PlaceType
import io.edugma.domain.schedule.model.schedule.LessonsByTime
import io.edugma.domain.schedule.model.teacher.Teacher
import io.edugma.features.base.core.utils.Typed2Listener
import io.edugma.features.base.elements.placeholder
import io.edugma.features.schedule.R
import io.edugma.features.schedule.model.ScheduleDayUiModel
import io.edugma.features.schedule.model.ScheduleItem
import io.edugma.features.schedule.utils.toUiModel
import java.time.LocalDate
import java.time.LocalTime

private val relaxAnims = listOf(
    R.raw.sch_relax_0,
    R.raw.sch_relax_1,
    R.raw.sch_relax_2
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SchedulePager(
    scheduleDays: List<ScheduleDayUiModel>,
    lessonDisplaySettings: LessonDisplaySettings,
    pagerState: PagerState,
    onLessonClick: Typed2Listener<Lesson, LessonDateTime>
) {
    HorizontalPager(
        count = scheduleDays.size,
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) {
        val scheduleDay by remember(scheduleDays, it) { mutableStateOf(scheduleDays[it]) }

        if (scheduleDay.lessons.isNotEmpty()) {
            LessonList(
                lessons = scheduleDay.lessons,
                lessonDisplaySettings = lessonDisplaySettings,
                onLessonClick = { lesson, time ->
                    onLessonClick(lesson, LessonDateTime(scheduleDay.date, null, time))
                }
            )
        } else {
            val randomAnim = remember { relaxAnims[it % relaxAnims.size] }
            NoLessonsDay(randomAnim)
        }
    }
}

@Composable
fun NoLessonsDay(@RawRes animation: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animation))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Column {
        Spacer(modifier = Modifier.weight(1f))
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier
                .weight(5f)
        )
        Text(
            text = stringResource(R.string.sch_no_lessons_today),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .weight(4f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 10.dp),
            softWrap = true,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DateContent(date: LocalDate) {
    Text(date.toString())
}

@Composable
fun LessonList(
    lessons: List<ScheduleItem>,
    lessonDisplaySettings: LessonDisplaySettings,
    isLoading: Boolean = false,
    onLessonClick: Typed2Listener<Lesson, LessonTime>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        for (item in lessons) {
            when (item) {
                is ScheduleItem.LessonByTime -> {
                    LessonPlace(
                        lessonsByTime = item.lesson,
                        lessonDisplaySettings = lessonDisplaySettings,
                        isLoading = isLoading,
                        onLessonClick = { lesson, lessonTime ->
                            onLessonClick(lesson, lessonTime)
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


private fun LazyListScope.LessonPlace(
    lessonsByTime: LessonsByTime,
    lessonDisplaySettings: LessonDisplaySettings,
    isLoading: Boolean = false,
    onLessonClick: Typed2Listener<Lesson, LessonTime>
) {
    item {
        LessonTimeContent(lessonsByTime.time, isLoading)
    }
    items(lessonsByTime.lessons) { lesson ->
        LessonContent(
            lesson = lesson,
            displaySettings = lessonDisplaySettings,
            isLoading = isLoading,
            onLessonClick = { onLessonClick(it, lessonsByTime.time) }
        )
    }
}

@Composable
private fun LessonTimeContent(lessonTime: LessonTime, isLoading: Boolean = false) {
    Text(
        text = "${lessonTime.start} - ${lessonTime.end}",
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .padding(horizontal = 34.dp)
            .placeholder(visible = isLoading)
    )
}

@Composable
fun ScheduleDayPlaceHolder() {
    val lessons = List(3) {
        LessonsByTime(
            time = LessonTime(LocalTime.now(), LocalTime.now()),
            lessons = listOf(
                Lesson(
                    LessonSubject("", ""),
                    io.edugma.domain.schedule.model.lesson_type.LessonType("", "Qwerty qwerty", false),
                    listOf(Teacher("", "")),
                    listOf(Group("", "")),
                    listOf(Place("", "", PlaceType.Undefined, ""))
                )
            )
        )
    }

    LessonList(
        lessons = lessons.toUiModel()
            .filterIsInstance<ScheduleItem.LessonByTime>(),
        lessonDisplaySettings = LessonDisplaySettings.Default,
        isLoading = true
    ) { _, _ -> }
}