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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import io.edugma.domain.base.utils.capitalized
import io.edugma.domain.schedule.model.lesson.LessonTime
import io.edugma.domain.schedule.model.lesson_type.LessonType
import io.edugma.domain.schedule.model.review.LessonDates
import io.edugma.domain.schedule.model.review.LessonReviewUnit
import io.edugma.domain.schedule.model.review.LessonTimesReview
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.elements.PrimaryTopAppBar
import io.edugma.features.base.elements.SpacerHeight
import io.edugma.features.base.elements.TonalCard
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
            .padding(horizontal = 16.dp, vertical = 4.dp)
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
            lessonTimesReview.days.forEach { lessonTimesReviewByType ->
                Spacer(Modifier.height(10.dp))
                TonalCard(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 3.dp
                ) {
                    Column(Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)) {
                        LessonTypeContent(
                            type = lessonTimesReviewByType.lessonType
                        )
                        Spacer(Modifier.height(8.dp))
                        FlowRow(
                            mainAxisSpacing = 16.dp,
                            crossAxisSpacing = 8.dp
                        ) {
                            lessonTimesReviewByType.days.forEach { lessonReviewDay ->
                                RegularLessonTime(lessonReviewDay)
                            }
                        }
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

private val weekFormat = DateTimeFormatter.ofPattern("EEEE")

@Composable
fun RegularLessonTime(lessonReviewDay: LessonReviewUnit) {
    Column {
        DateRange(
            dayOfWeek = lessonReviewDay.dayOfWeek,
            dates = lessonReviewDay.dates,
            times = lessonReviewDay.time
        )
    }
}


private val dateFormatDay = DateTimeFormatter.ofPattern("d")
// TODO: Replace by LLL when desugar_jdk_libs 1.2.0 will be released
private val dateFormatMonth = DateTimeFormatter.ofPattern("MMM")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRange(
    dayOfWeek: DayOfWeek,
    dates: List<LessonDates>,
    times: List<LessonTime>
) {
    Surface(
        color = MaterialTheme3.colorScheme.surfaceVariant,
        shape = MaterialTheme3.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Dates(dates = dates)
            Divider(
                Modifier
                    .padding(horizontal = 10.dp)
                    .width(1.dp)
                    .height(30.dp)
                    .align(Alignment.CenterVertically),
            )
            Column {
                Text(
                    text = weekFormat.format(dayOfWeek).capitalized(),
                    style = MaterialTheme.typography.bodyMedium
                )
                times.forEach { time ->
                    Text(
                        text = "${time.start} - ${time.end}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun Dates(dates: List<LessonDates>) {
    Column {
        dates.forEach { (dateFrom, dateTo) ->
            Row {
                Column(Modifier.width(IntrinsicSize.Max)) {
                    Text(
                        text = dateFrom.format(dateFormatMonth)
                            .replace(".", "")
                            .capitalized(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = dateFrom.format(dateFormatDay),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                if (dateTo != null) {
                    Divider(
                        Modifier
                            .padding(horizontal = 8.dp)
                            .width(10.dp)
                            .height(1.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Column(Modifier.width(IntrinsicSize.Max)) {
                        Text(
                            text = dateTo.format(dateFormatMonth)
                                .replace(".", "")
                                .capitalized(),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = dateTo.format(dateFormatDay),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}