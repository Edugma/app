package io.edugma.features.schedule.daily.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edugma.shared.core.resources.generated.resources.Res
import edugma.shared.core.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import io.edugma.core.designSystem.atoms.lottie.EdLottie
import io.edugma.core.designSystem.atoms.lottie.LottieSource
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.utils.ClickListener
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import io.edugma.features.schedule.elements.lesson.LessonPlace
import io.edugma.features.schedule.elements.lesson.LessonPlaceholder
import io.edugma.features.schedule.elements.lesson.LessonWindow
import io.edugma.features.schedule.elements.lesson.model.ScheduleEventUiModel
import io.edugma.features.schedule.elements.model.ScheduleCalendarUiModel
import kotlinx.datetime.LocalDate

private val relaxAnims = listOf(
    "files/sch_relax_0.json",
    "files/sch_relax_1.json",
    "files/sch_relax_2.json",
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SchedulePager(
    scheduleDays: ScheduleCalendarUiModel,
    lessonDisplaySettings: LessonDisplaySettings,
    isRefreshing: Boolean,
    pagerState: PagerState,
    onLessonClick: (LessonEvent) -> Unit,
    onRefreshing: ClickListener,
) {
    EdPullRefresh(
        refreshing = isRefreshing,
        onRefresh = onRefreshing,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val scheduleDay by remember(scheduleDays, page) { mutableStateOf(scheduleDays[page]) }

            if (scheduleDay.lessons.isNotEmpty()) {
                LessonList(
                    lessons = scheduleDay.lessons,
                    lessonDisplaySettings = lessonDisplaySettings,
                    onLessonClick = onLessonClick,
                )
            } else {
                val randomAnim = remember { relaxAnims[page % relaxAnims.size] }
                NoLessonsDay(
                    animation = randomAnim,
                )
            }
        }
    }
}

@Composable
fun NoLessonsDay(
    animation: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
        EdLottie(
            lottieSource = LottieSource.FileRes(animation),
            modifier = Modifier
                .weight(5f)
                .fillMaxWidth(),
        )
        Text(
            text = stringResource(Res.string.sch_no_lessons_today),
            style = EdTheme.typography.titleLarge,
            modifier = Modifier
                .weight(4f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 10.dp),
            softWrap = true,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun DateContent(date: LocalDate) {
    Text(date.toString())
}

@Composable
fun LessonList(
    lessons: List<ScheduleEventUiModel>,
    lessonDisplaySettings: LessonDisplaySettings,
    onLessonClick: (LessonEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        for (item in lessons) {
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
        item {
            Spacer(modifier = Modifier.navigationBarsPadding().height(30.dp))
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun ScheduleDayPlaceholder(size: Int) {
    Column(Modifier.fillMaxSize()) {
        (0..<size).forEach {
            LessonPlaceholder()
            SpacerHeight(8.dp)
        }
    }
}
