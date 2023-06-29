package io.edugma.features.schedule.daily

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
import com.moriatsushi.insetsx.navigationBarsPadding
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.designSystem.atoms.lottie.EdLottie
import io.edugma.core.designSystem.atoms.lottie.LottieSource
import io.edugma.core.designSystem.atoms.lottie.rememberLottiePainter
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed2Listener
import io.edugma.domain.base.utils.nowLocalTime
import io.edugma.features.schedule.daily.resources.MR
import io.edugma.features.schedule.domain.model.group.Group
import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonDateTime
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import io.edugma.features.schedule.domain.model.lessonSubject.LessonSubject
import io.edugma.features.schedule.domain.model.lessonType.LessonType
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.model.place.PlaceType
import io.edugma.features.schedule.domain.model.schedule.LessonsByTime
import io.edugma.features.schedule.domain.model.teacher.Teacher
import io.edugma.features.schedule.elements.lesson.LessonPlace
import io.edugma.features.schedule.elements.lesson.LessonWindow
import io.edugma.features.schedule.elements.lesson.model.ScheduleItem
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import io.edugma.features.schedule.elements.utils.toUiModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

private val relaxAnims = listOf(
    MR.assets.sch_relax_0,
    MR.assets.sch_relax_1,
    MR.assets.sch_relax_2,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SchedulePager(
    scheduleDays: List<ScheduleDayUiModel>,
    lessonDisplaySettings: LessonDisplaySettings,
    isRefreshing: Boolean,
    pagerState: PagerState,
    onLessonClick: Typed2Listener<Lesson, LessonDateTime>,
    onRefreshing: ClickListener,
) {
    EdPullRefresh(
        refreshing = isRefreshing,
        onRefresh = onRefreshing,
    ) {
        HorizontalPager(
            pageCount = scheduleDays.size,
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
    animation: AssetResource,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
        val painter = rememberLottiePainter(
            source = LottieSource.Asset(animation),
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
    onLessonClick: Typed2Listener<Lesson, LessonTime>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        for (item in lessons) {
            when (item) {
                is ScheduleItem.LessonByTime -> {
                    LessonPlace(
                        lessonsByTime = item.lesson,
                        lessonDisplaySettings = lessonDisplaySettings,
                        isLoading = isLoading,
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

@Composable
fun ScheduleDayPlaceHolder() {
    val lessons = List(3) {
        LessonsByTime(
            time = LessonTime(Clock.System.nowLocalTime(), Clock.System.nowLocalTime()),
            lessons = listOf(
                Lesson(
                    LessonSubject("", ""),
                    LessonType("", "Qwerty qwerty", false),
                    listOf(Teacher("", "", "")),
                    listOf(Group("", "", "")),
                    listOf(Place("", "", PlaceType.Undefined, "")),
                ),
            ),
        )
    }

    LessonList(
        lessons = lessons.toUiModel()
            .filterIsInstance<ScheduleItem.LessonByTime>(),
        lessonDisplaySettings = LessonDisplaySettings.Default,
        isLoading = true,
        onLessonClick = { _, _ -> },
    )
}
