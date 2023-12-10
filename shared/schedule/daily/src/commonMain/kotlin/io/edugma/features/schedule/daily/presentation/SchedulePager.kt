package io.edugma.features.schedule.daily.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.designSystem.atoms.lottie.EdLottie
import io.edugma.core.designSystem.atoms.lottie.LottieSource
import io.edugma.core.designSystem.atoms.lottie.rememberLottiePainter
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.navigationBarsPadding
import io.edugma.core.resources.MR
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed2Listener
import io.edugma.features.schedule.domain.model.lesson.LessonDateTime
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import io.edugma.features.schedule.elements.lesson.LessonPlace
import io.edugma.features.schedule.elements.lesson.LessonPlaceholder
import io.edugma.features.schedule.elements.lesson.LessonWindow
import io.edugma.features.schedule.elements.lesson.model.ScheduleItem
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import kotlinx.datetime.LocalDate

private val relaxAnims = listOf(
    MR.files.sch_relax_0,
    MR.files.sch_relax_1,
    MR.files.sch_relax_2,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SchedulePager(
    scheduleDays: List<ScheduleDayUiModel>,
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
                    onLessonClick = { lesson, time ->
                        onLessonClick(lesson, LessonDateTime(scheduleDay.date, null, time))
                    },
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
    animation: FileResource,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
        val painter = rememberLottiePainter(
            source = LottieSource.FileRes(animation),
            alternativeUrl = "https://raw.githubusercontent.com/Edugma/resources/main/42410-sleeping-polar-bear.gif",
        )
        EdLottie(
            lottiePainter = painter,
            modifier = Modifier
                .weight(5f)
                .fillMaxWidth(),
        )
        Text(
            text = stringResource(MR.strings.sch_no_lessons_today),
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
    lessons: List<ScheduleItem>,
    lessonDisplaySettings: LessonDisplaySettings,
    onLessonClick: Typed2Listener<LessonEvent, LessonTime>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        for (item in lessons) {
            when (item) {
                is ScheduleItem.LessonEventUiModel -> {
                    LessonPlace(
                        lessonEvent = item.lesson2,
                        lessonDisplaySettings = lessonDisplaySettings,
                        onLessonClick = { lesson, lessonTime ->
                            onLessonClick(lesson, lessonTime)
                        },
                    )
                }
                is ScheduleItem.Window -> {
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
