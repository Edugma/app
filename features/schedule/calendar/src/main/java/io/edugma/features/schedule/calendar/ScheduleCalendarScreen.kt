package io.edugma.features.schedule.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.domain.base.utils.capitalized
import io.edugma.features.base.core.utils.*
import io.edugma.features.schedule.calendar.model.ScheduleCalendarWeek
import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import org.koin.androidx.compose.getViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleCalendarScreen(viewModel: ScheduleCalendarViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    ScheduleCalendarContent(
        state = state,
        onBackClick = viewModel::exit,
        onItemClick = viewModel::onDayClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleCalendarContent(
    state: ScheduleCalendarState,
    onBackClick: ClickListener,
    onItemClick: Typed1Listener<LocalDate>,
) {
    var cellCount by remember { mutableStateOf(3) }
//    val q = rememberTransformableState { zoomChange, _, _ ->
//        Log.d("ZOOM", zoomChange.toString())
//        cellCount = (cellCount + ((1f - zoomChange) / 0.05f).toInt()).coerceIn(1..6)
//    }

    Column() {
        EdTopAppBar(
            title = stringResource(R.string.schedule_cal_calendar),
            onNavigationClick = onBackClick,
        )
        CalendarThree(
            schedule = state.schedule,
            onItemClick = onItemClick,
        )
    }
}

private val dateFormat = DateTimeFormatter.ofPattern("EEE, d MMM")

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CalendarThree(
    schedule: List<ScheduleCalendarWeek>,
    onItemClick: Typed1Listener<LocalDate>,
) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(schedule) { week ->
            SpacerHeight(8.dp)
            Surface {
                Column(Modifier.fillMaxWidth().padding(horizontal = 2.dp)) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                    ) {
                        WeekNumber(
                            weekNumber = week.weekNumber,
                            modifier = Modifier.weight(1f),
                        )
                        CalendarItem(
                            day = week.schedule[0],
                            modifier = Modifier.weight(1f),
                            onItemClick = { onItemClick(week.schedule[0].date) },
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                    ) {
                        CalendarItem(
                            day = week.schedule[1],
                            modifier = Modifier.weight(1f),
                            onItemClick = { onItemClick(week.schedule[1].date) },
                        )
                        CalendarItem(
                            day = week.schedule[2],
                            modifier = Modifier.weight(1f),
                            onItemClick = { onItemClick(week.schedule[2].date) },
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                    ) {
                        CalendarItem(
                            day = week.schedule[3],
                            modifier = Modifier.weight(1f),
                            onItemClick = { onItemClick(week.schedule[3].date) },
                        )
                        CalendarItem(
                            day = week.schedule[4],
                            modifier = Modifier.weight(1f),
                            onItemClick = { onItemClick(week.schedule[4].date) },
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                    ) {
                        CalendarItem(
                            day = week.schedule[5],
                            modifier = Modifier.weight(1f),
                            onItemClick = { onItemClick(week.schedule[5].date) },
                        )
                        CalendarItem(
                            day = week.schedule[6],
                            modifier = Modifier.weight(1f),
                            onItemClick = { onItemClick(week.schedule[6].date) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekNumber(
    weekNumber: Int,
    modifier: Modifier = Modifier,
) {
//    Column(modifier) {
//        Text(
//            text = "Неделя",
//            style = EdTheme.typography.labelMedium
//        )
//        Text(
//            text = (weekNumber + 1).toString(),
//            style = EdTheme.typography.titleLarge
//        )
//    }

    Box(
        modifier = modifier.fillMaxHeight().padding(bottom = 12.dp, top = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            Text(
                text = "Неделя".uppercase(),
                style = EdTheme.typography.labelMedium,
                color = EdTheme.colorScheme.tertiary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Text(
                text = (weekNumber + 1).toString(),
                style = EdTheme.typography.displayMedium,
                color = EdTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = -EdTheme.typography.displayMedium.fontSize.dp() * 0.1f)
                    .height(EdTheme.typography.displayMedium.fontSize.dp() * 1.1f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarItem(
    day: ScheduleDay,
    onItemClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdCard(
        onClick = onItemClick,
        modifier = modifier
            .fillMaxHeight()
            .padding(start = 7.dp, end = 7.dp, top = 7.dp, bottom = 7.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 14.dp, start = 12.dp, end = 12.dp),
        ) {
            Text(
                text = dateFormat.format(day.date).capitalized(),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                color = EdTheme.colorScheme.secondary,
            )

            if (day.lessons.isNotEmpty()) {
                SpacerHeight(6.dp)
            }
            day.lessons.forEachIndexed { index, lessonsByTime ->
                if (index != 0) {
                    SpacerHeight(5.dp)
                }
                Text(
                    text = "•" + lessonsByTime.time.start.toString() + " - " + lessonsByTime.time.end.toString(),
                    style = MaterialTheme.typography.labelMedium,
                )
                WithContentAlpha(ContentAlpha.medium) {
                    val text = buildAnnotatedString {
                        lessonsByTime.lessons.forEachIndexed { index, lesson ->
                            append(cutTitle(lesson.subject.title))
                            append(" (")
                            if (lesson.type.isImportant) {
                                pushStyle(SpanStyle(color = EdTheme.colorScheme.error))
                                append(getShortType(lesson.type.title))
                                pop()
                            } else {
                                append(getShortType(lesson.type.title))
                            }
                            append(")")
                            if (index != lessonsByTime.lessons.lastIndex) {
                                append("\n")
                            }
                        }
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

private const val minCriticalTitleLength = 10
private const val minCriticalWordLength = 5

private const val vowels = "аеёиоуыэьъюя"
private const val specChars = "ьъ"

// TODO: Fix ьъ
fun cutTitle(title: String): String {
    if (title.length <= minCriticalTitleLength) {
        return title
    }
    val words = title.split(' ').filter { it.isNotEmpty() }
    if (words.size == 1) {
        return words.first()
    }

    return words.joinToString(" ") { cutWord(it) }
}

private fun cutWord(word: String): String {
    if (word.length <= minCriticalWordLength) {
        return word
    }

    val vowelIndex = word.indexOfFirst { vowels.contains(it, ignoreCase = true) }
    if (vowelIndex == -1) {
        return word
    }
    for (i in vowelIndex + 1 until word.length) {
        // TODO: Fix for spec chars
        if (vowels.contains(word[i], ignoreCase = true) && !specChars.contains(word[i], ignoreCase = true)) {
            // if two vowels are near or shorted word will be too short
            if (i == vowelIndex + 1 || i < 3) {
                continue
            }
            return word.substring(0, i) + '.'
        }
    }
    return word
}

fun abbreviationFrom(title: String): String {
    val words = title.split(' ').filter { it.isNotEmpty() }
    if (words.size == 1) {
        return cutWord(words.first())
    }
    val cutWords = words.map { it.first().toUpperCase() }

    return cutWords.joinToString("")
}

fun getShortType(type: String): String {
    return type.split(' ').filter { it.isNotEmpty() }.joinToString(" ") { cutWord(it) }
}
