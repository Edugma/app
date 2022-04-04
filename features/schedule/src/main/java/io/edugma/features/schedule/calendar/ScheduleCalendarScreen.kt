package io.edugma.features.schedule.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.edugma.domain.base.utils.capitalized
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.ContentAlpha
import io.edugma.features.base.core.utils.WithContentAlpha
import io.edugma.features.base.elements.PrimaryTopAppBar
import io.edugma.features.schedule.R
import org.koin.androidx.compose.getViewModel
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleCalendarScreen(viewModel: ScheduleCalendarViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    ScheduleCalendarContent(state, viewModel::exit)
}

@Composable
fun ScheduleCalendarContent(
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
            title = stringResource(R.string.sch_calendar),
            onBackClick = onBackClick
        )
        CalendarThree(schedule = state.schedule, 3)
    }
}

private val dateFormat = DateTimeFormatter.ofPattern("EEE, d MMM")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarThree(schedule: List<ScheduleDay>, cellCount: Int) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(cellCount)
    ) {
        items(schedule) { day ->
            Column(Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = dateFormat.format(day.date).capitalized(),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Divider(Modifier.height(1.dp))
                day.lessons.forEach { lessonsByTime ->
                    Text(
                        text = lessonsByTime.time.startTime.toString() + " - " + lessonsByTime.time.endTime.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    WithContentAlpha(ContentAlpha.medium) {
                        Text(
                            text = lessonsByTime.lessons.joinToString(separator = "\n") { cutTitle(it.title) + " (${getShortType(it.type)})" },
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
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
