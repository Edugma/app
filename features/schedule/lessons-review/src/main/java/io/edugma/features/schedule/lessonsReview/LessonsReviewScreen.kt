package io.edugma.features.schedule.lessonsReview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.domain.base.utils.capitalized
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import io.edugma.features.schedule.domain.model.lesson_type.LessonType
import io.edugma.features.schedule.domain.model.review.LessonDates
import io.edugma.features.schedule.domain.model.review.LessonTimesReview
import io.edugma.features.schedule.domain.model.review.LessonTimesReviewByType
import io.edugma.features.schedule.lessons_review.R
import org.koin.androidx.compose.getViewModel
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter

@Composable
fun LessonsReviewScreen(
    viewModel: LessonsReviewViewModel = getViewModel(),
) {
    val state by viewModel.state.collectAsState()

    FeatureScreen {
        LessonsReviewContent(
            lessons = state.lessons,
            onBackClick = viewModel::exit,
        )
    }
}

@Composable
fun LessonsReviewContent(
    lessons: List<LessonTimesReview>,
    onBackClick: ClickListener,
) {
    Column(Modifier.fillMaxSize()) {
        EdTopAppBar(
            title = stringResource(R.string.schedule_les_rev_lessons_review),
            onNavigationClick = onBackClick,
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(lessons) { lessonTimesReview ->
                LessonTimesReviewContent(lessonTimesReview)
                SpacerHeight(12.dp)
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
            .fillMaxWidth(),
    ) {
        Text(
            text = lessonTimesReview.subject.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
        )
        SpacerHeight(2.dp)
        Column(
            Modifier.fillMaxWidth(),
        ) {
            lessonTimesReview.days.forEach { item ->
                SpacerHeight(12.dp)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                ) {
                    DatesAndTimeUnit(
                        item,
                        // modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DatesAndTimeUnit(
    lessonTimesReviewByType: LessonTimesReviewByType,
    modifier: Modifier = Modifier,
) {
    EdCard(
        modifier = modifier.fillMaxHeight(),
    ) {
        Column(Modifier.padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 12.dp)) {
            LessonTypeContent(
                type = lessonTimesReviewByType.lessonType,
            )
            SpacerHeight(12.dp)
            lessonTimesReviewByType.days.forEachIndexed { index, lessonReviewUnit ->
                if (index != 0) {
                    Divider(
                        Modifier
                            .height(1.dp)
                            .fillMaxWidth(),
                    )
                }
                DateRange(
                    dayOfWeek = lessonReviewUnit.dayOfWeek,
                    dates = lessonReviewUnit.dates,
                    times = lessonReviewUnit.time,
                )
            }
        }
    }
}

@Composable
fun LessonTypeContent(type: LessonType) {
    val color = if (type.isImportant) {
        EdTheme.colorScheme.error
    } else {
        Color.Unspecified
    }
    Text(
        text = type.title,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        color = color,
        textAlign = TextAlign.Center,
    )
}

private val weekFormat = DateTimeFormatter.ofPattern("EEE")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRange(
    dayOfWeek: DayOfWeek,
    dates: List<LessonDates>,
    times: List<LessonTime>,
) {
    Row(
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
    ) {
        Text(
            text = weekFormat.format(dayOfWeek).capitalized(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .padding(bottom = 8.dp, top = 6.dp),
        )
        Divider(
            Modifier
                .padding(horizontal = 10.dp)
                .width(1.dp)
                .fillMaxHeight(),
        )
        Dates(
            dates = dates,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(6f)
                .padding(bottom = 8.dp, top = 6.dp),
        )
        Divider(
            Modifier
                .padding(horizontal = 10.dp)
                .width(1.dp)
                .fillMaxHeight(),
        )
        Times(
            times = times,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(6f)
                .padding(bottom = 8.dp, top = 6.dp),
        )
    }
}

private val dateFormat = DateTimeFormatter.ofPattern("d MMM")

@Composable
private fun Dates(
    dates: List<LessonDates>,
    modifier: Modifier = Modifier,
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        dates.forEach { date ->
            val dateFrom = date.start.format(dateFormat)
            var dateText = dateFrom

            if (date.end != null) {
                dateText += " - " + date.end!!.format(dateFormat)
            }

            Text(
                text = dateText,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

@Composable
private fun Times(
    times: List<LessonTime>,
    modifier: Modifier = Modifier,
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        times.forEach { time ->
            val timeFrom = time.start.format(timeFormat)
            val timeTo = time.end.format(timeFormat)
            val timeText = "$timeFrom - $timeTo"

            Text(
                text = timeText,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
