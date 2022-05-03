package io.edugma.features.schedule.lessons_review

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.domain.base.utils.capitalized
import io.edugma.domain.schedule.model.lesson.LessonTime
import io.edugma.domain.schedule.model.lesson_type.LessonType
import io.edugma.domain.schedule.model.review.LessonDates
import io.edugma.domain.schedule.model.review.LessonReviewUnit
import io.edugma.domain.schedule.model.review.LessonTimesReview
import io.edugma.domain.schedule.model.review.LessonTimesReviewByType
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.elements.*
import org.koin.androidx.compose.getViewModel
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter

@Composable
fun LessonsReviewScreen(
    viewModel: LessonsReviewViewModel = getViewModel()
) {
    val state by viewModel.state.collectAsState()

    LessonsReviewContent(state.lessons, viewModel::exit)
}

@Composable
fun LessonsReviewContent(
    lessons: List<LessonTimesReview>,
    onBackClick: ClickListener
) {
    Column(Modifier.fillMaxSize()) {
        PrimaryTopAppBar(
            title = stringResource(R.string.schedule_les_rev_lessons_review),
            onBackClick = onBackClick
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(lessons) { lessonTimesReview ->
                LessonTimesReviewContent(lessonTimesReview)
                SpacerHeight(8.dp)
            }
            item {
                SpacerHeight(4.dp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonTimesReviewContent(lessonTimesReview: LessonTimesReview) {
    Column(
        Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = lessonTimesReview.subject.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
        SpacerHeight(2.dp)
        Column(
            Modifier.fillMaxWidth()
        ) {
            var res = lessonTimesReview.days.asSequence<LessonTimesReviewByType?>()

            if (lessonTimesReview.days.size % 2 == 1) {
                res += null
            }

            res.zipWithNext().forEach { (first, second) ->
                SpacerHeight(8.dp)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    if (first == null) {
                        SpacerFill()
                    } else {
                        Qwerty(
                            first,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    SpacerWidth(8.dp)
                    if (second == null) {
                        SpacerFill()
                    } else {
                        Qwerty(
                            second,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Qwerty(
    lessonTimesReviewByType: LessonTimesReviewByType,
    modifier: Modifier = Modifier
) {
    TonalCard(
        modifier = modifier.fillMaxHeight(),
        tonalElevation = 3.dp
    ) {
        Column(Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)) {
            LessonTypeContent(
                type = lessonTimesReviewByType.lessonType
            )
            Spacer(Modifier.height(8.dp))
            Column {
                lessonTimesReviewByType.days.forEachIndexed { index, lessonReviewUnit ->
                    DateRange(
                        dayOfWeek = lessonReviewUnit.dayOfWeek,
                        dates = lessonReviewUnit.dates,
                        times = lessonReviewUnit.time
                    )
                    if (index != lessonTimesReviewByType.days.lastIndex) {
                        SpacerHeight(8.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun LessonTypeContent(type: LessonType) {
    val color = if (type.isImportant) {
        MaterialTheme3.colorScheme.error
    } else {
        Color.Unspecified
    }
    Text(
        text = type.title,
        style = MaterialTheme.typography.titleSmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(horizontal = 10.dp),
        color = color
    )
}

private val weekFormat = DateTimeFormatter.ofPattern("EEE")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRange(
    dayOfWeek: DayOfWeek,
    dates: List<LessonDates>,
    times: List<LessonTime>
) {
    TonalCard(
        color = MaterialTheme3.colorScheme.surfaceVariant,
        shape = MaterialTheme3.shapes.small,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                .height(IntrinsicSize.Max)
                .fillMaxWidth()
        ) {
            Text(
                text = weekFormat.format(dayOfWeek).capitalized(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Divider(
                Modifier
                    .padding(horizontal = 4.dp)
                    .width(1.dp)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Dates(dates = dates)
                Divider(
                    Modifier
                        .padding(vertical = 4.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                )
                Times(times = times)
            }
        }
    }
}

private val dateFormat = DateTimeFormatter.ofPattern("d MMM")

@Composable
private fun Dates(dates: List<LessonDates>) {
    Column {
        dates.forEach { date ->
            val dateFrom = date.start.format(dateFormat)
            var dateText = dateFrom

            if (date.end != null) {
                dateText += " - " + date.end!!.format(dateFormat)
            }

            Text(
                text = dateText,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

@Composable
private fun Times(times: List<LessonTime>) {
    Column {
        times.forEach { time ->
            val timeFrom = time.start.format(timeFormat)
            val timeTo = time.end.format(timeFormat)
            val timeText = "$timeFrom - $timeTo"

            Text(
                text = timeText,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}