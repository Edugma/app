package com.edugma.features.schedule.daily.presentation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import edugma.shared.core.resources.generated.resources.Res
import edugma.shared.core.resources.generated.resources.*
import com.edugma.core.api.model.LceUiState
import org.jetbrains.compose.resources.stringResource
import com.edugma.core.designSystem.atoms.lottie.EdLottie
import com.edugma.core.designSystem.atoms.lottie.LottieSource
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.utils.ClickListener
import com.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import com.edugma.features.schedule.domain.model.lesson.LessonEvent
import com.edugma.features.schedule.elements.lesson.LessonPlace
import com.edugma.features.schedule.elements.lesson.LessonPlaceholder
import com.edugma.features.schedule.elements.lesson.LessonWindow
import com.edugma.features.schedule.elements.lesson.model.ScheduleEventUiModel
import com.edugma.features.schedule.elements.model.ScheduleCalendarUiModel
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
    lceState: LceUiState,
    pagerState: PagerState,
    onLessonClick: (LessonEvent) -> Unit,
    onRefresh: ClickListener,
) {
    EdPullRefresh(
        refreshing = lceState.isRefreshing,
        onRefresh = onRefresh,
    ) {
        HorizontalPager(
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                // TODO remove after update https://issuetracker.google.com/issues/338710348
                snapAnimationSpec = spring(
                    stiffness = Spring.StiffnessMediumLow,
                    visibilityThreshold = Int.VisibilityThreshold.toFloat()
                ),
            ),
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
                var offset = remember { mutableStateOf(0f) }
                // TODO Lambda memoization
                val scrollState = rememberScrollableState(remember { { 0f } })
                NoLessonsDay(
                    animation = randomAnim,
                    modifier = Modifier.scrollable(
                        orientation = Orientation.Vertical,
                        state = scrollState,
                    ).offset {
                        IntOffset(0, offset.value.toInt())
                    }
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
        repeat(size) {
            LessonPlaceholder()
            SpacerHeight(8.dp)
        }
    }
}
