package io.edugma.features.schedule.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.edugma.domain.base.utils.capitalized
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.PrimaryTopAppBar
import io.edugma.features.base.elements.SpacerHeight
import io.edugma.features.schedule.calendar.model.ScheduleCalendarWeek
import org.koin.androidx.compose.getViewModel
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleCalendarScreen(viewModel: ScheduleCalendarViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    ScheduleCalendarContent(state, viewModel::exit)
}

@Composable
private fun ScheduleCalendarContent(
    state: ScheduleCalendarState,
    onBackClick: ClickListener
) {
    var cellCount by remember { mutableStateOf(3) }
//    val q = rememberTransformableState { zoomChange, _, _ ->
//        Log.d("ZOOM", zoomChange.toString())
//        cellCount = (cellCount + ((1f - zoomChange) / 0.05f).toInt()).coerceIn(1..6)
//    }


    Column(
    ) {
        PrimaryTopAppBar(
            title = stringResource(R.string.schedule_cal_calendar),
            onBackClick = onBackClick
        )
        CalendarThree(schedule = state.schedule)
    }
}

private val dateFormat = DateTimeFormatter.ofPattern("EEE, d MMM")

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CalendarThree(schedule: List<ScheduleCalendarWeek>) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(schedule) { week ->
            SpacerHeight(8.dp)
            Surface(

            ) {
                Column(Modifier.fillMaxWidth()) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)) {
                        WeekNumber(
                            weekNumber = week.weekNumber,
                            modifier = Modifier.weight(1f)
                        )
                        CalendarItem(
                            day = week.schedule[0],
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)) {
                        CalendarItem(
                            day = week.schedule[1],
                            modifier = Modifier.weight(1f)
                        )
                        CalendarItem(
                            day = week.schedule[2],
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)) {
                        CalendarItem(
                            day = week.schedule[3],
                            modifier = Modifier.weight(1f)
                        )
                        CalendarItem(
                            day = week.schedule[4],
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)) {
                        CalendarItem(
                            day = week.schedule[5],
                            modifier = Modifier.weight(1f)
                        )
                        CalendarItem(
                            day = week.schedule[6],
                            modifier = Modifier.weight(1f)
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
    modifier: Modifier = Modifier
) {
//    Column(modifier) {
//        Text(
//            text = "Неделя",
//            style = MaterialTheme3.typography.labelMedium
//        )
//        Text(
//            text = (weekNumber + 1).toString(),
//            style = MaterialTheme3.typography.titleLarge
//        )
//    }

    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "Неделя".uppercase(),
                style = MaterialTheme3.typography.labelMedium,
                color = MaterialTheme3.colorScheme.tertiary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = (weekNumber + 1).toString(),
                style = MaterialTheme3.typography.displayMedium,
                color = MaterialTheme3.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = -MaterialTheme3.typography.displayMedium.fontSize.dp() * 0.1f)
                    .height(MaterialTheme3.typography.displayMedium.fontSize.dp() * 1.1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarItem(
    day: ScheduleDay,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = {  },
        modifier = modifier
            .fillMaxHeight()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 8.dp)
        ) {
            Text(
                text = dateFormat.format(day.date).capitalized(),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
            SpacerHeight(4.dp)
            WithContentAlpha(alpha = ContentAlpha.disabled) {
                Divider(
                    Modifier.height(2.dp), 
                    color = LocalContentColor.current
                )
            }
            if (day.lessons.isNotEmpty()) {
                SpacerHeight(4.dp)
            }
            day.lessons.forEach { lessonsByTime ->
                Text(
                    text = lessonsByTime.time.start.toString() + " - " + lessonsByTime.time.end.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                WithContentAlpha(ContentAlpha.medium) {
                    Text(
                        text = lessonsByTime.lessons
                            .joinToString(separator = "\n") {
                                cutTitle(it.title) + " (${getShortType(it.type)})"
                            },
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp)
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
