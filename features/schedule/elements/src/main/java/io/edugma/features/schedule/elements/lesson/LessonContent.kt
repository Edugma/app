package io.edugma.features.schedule.elements.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import io.edugma.domain.schedule.model.group.Group
import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonDisplaySettings
import io.edugma.domain.schedule.model.lesson_subject.LessonSubject
import io.edugma.domain.schedule.model.lesson_type.LessonType
import io.edugma.domain.schedule.model.place.Place
import io.edugma.domain.schedule.model.teacher.Teacher
import io.edugma.domain.schedule.utils.getShortName
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.SpacerHeight
import io.edugma.features.base.elements.TonalCard
import io.edugma.features.base.elements.placeholder
import io.edugma.features.schedule.elements.lesson.model.ScheduleItem
import java.time.format.DateTimeFormatter

@Composable
fun LessonContent(
    lesson: Lesson,
    displaySettings: LessonDisplaySettings,
    isLoading: Boolean = false,
    onLessonClick: Typed1Listener<Lesson>
) {
    TonalCard(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .fillMaxWidth(),
        onClick = { onLessonClick(lesson) }
    ) {
        Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 13.dp, bottom = 16.dp)) {
            WithContentAlpha(ContentAlpha.medium) {
                LessonHeader(lesson.type, isLoading)
            }
            SpacerHeight(3.dp)
            WithContentAlpha(ContentAlpha.high) {
                LessonTitle(lesson.subject, isLoading)
            }
            SpacerHeight(4.dp)
            WithContentAlpha(ContentAlpha.medium) {
                if (displaySettings.showTeachers && lesson.teachers.isNotEmpty()) {
                    SpacerHeight(2.dp)
                    TeachersContent(lesson.teachers, isLoading)
                    SpacerHeight(2.dp)
                }
                if (displaySettings.showGroups && lesson.groups.isNotEmpty()) {
                    SpacerHeight(2.dp)
                    GroupsContent(lesson.groups, isLoading)
                    SpacerHeight(2.dp)
                }
                if (displaySettings.showPlaces && lesson.places.isNotEmpty()) {
                    SpacerHeight(2.dp)
                    PlacesContent(lesson.places, isLoading)
                    SpacerHeight(2.dp)
                }
            }
        }
    }
}

@Composable
fun LessonHeader(type: LessonType, isLoading: Boolean = false) {
    LessonType(type, isLoading)
}

@Composable
fun LessonType(type: LessonType, isLoading: Boolean = false) {
    val color = if (type.isImportant) {
        MaterialTheme3.colorScheme.error
    } else {
        Color.Unspecified
    }
    Text(
        text = type.title.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.placeholder(visible = isLoading),
        color = color
    )
}

@Composable
fun LessonTitle(subject: LessonSubject, isLoading: Boolean = false) {
    Text(
        text = subject.title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .placeholder(visible = isLoading)
    )
}

@Composable
fun TeachersContent(teachers: List<Teacher>, isLoading: Boolean = false) {
    Row(
        modifier = Modifier.placeholder(visible = isLoading)
    ) {
        Icon(
            painter = painterResource(id = FluentIcons.ic_fluent_hat_graduation_16_regular),
            contentDescription = null,
            modifier = Modifier
                .size(17.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(Modifier.width(5.dp))
        val teachersText = remember(teachers) {
            if (teachers.size == 1)
                teachers.first().name
            else
                teachers.joinToString { it.getShortName() }
        }
        Text(
            text = teachersText,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
        )
    }
}

@Composable
fun GroupsContent(groups: List<Group>, isLoading: Boolean = false) {
    Row(
        modifier = Modifier.placeholder(visible = isLoading)
    ) {
        Icon(
            painter = painterResource(id = FluentIcons.ic_fluent_people_16_regular),
            contentDescription = null,
            modifier = Modifier
                .size(17.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(Modifier.width(5.dp))
        val groupsText = remember(groups) { groups.joinToString { it.title } }
        Text(
            text = groupsText,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
        )
    }
}

@Composable
fun PlacesContent(places: List<Place>, isLoading: Boolean = false) {
    Row(
        modifier = Modifier.placeholder(visible = isLoading)
    ) {
        Icon(
            painter = painterResource(id = FluentIcons.ic_fluent_location_16_regular),
            contentDescription = null,
            modifier = Modifier
                .size(17.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(Modifier.width(5.dp))
        val placesText = remember(places) { places.joinToString { it.title } }
        Text(
            text = placesText,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
        )
    }
}

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun LessonWindow(lessonWindow: ScheduleItem.Window) {
    val timeFrom = remember(lessonWindow) { lessonWindow.timeFrom.format(timeFormatter) }
    val timeTo = remember(lessonWindow) { lessonWindow.timeTo.format(timeFormatter) }

    val timeText = remember(lessonWindow) {
        getTimeText(lessonWindow.totalMinutes)
    }

    Row(
        Modifier
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val urls = listOf(
            "https://img.icons8.com/fluency/200/cup.png",
            "https://img.icons8.com/fluency/192/kfc-chicken.png",
            "https://img.icons8.com/fluency/200/controller.png",
            "https://img.icons8.com/fluency/200/sunbathe.png"
        )
        val url = remember { urls.random() }

        Image(
            painter = rememberImagePainter(data = url),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(60.dp),
            contentScale = ContentScale.Inside,
            alignment = Alignment.Center
        )
        Column(
            modifier = Modifier
                .padding(start = 14.dp, end = 12.dp, top = 11.dp, bottom = 14.dp)
        ) {
            Text(
                text = "???????? ???? $timeText",
                style = MaterialTheme.typography.titleMedium
            )
            WithContentAlpha(alpha = ContentAlpha.medium) {
                Text(
                    text = "$timeFrom - $timeTo",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun getTimeText(totalMinutes: Long): String {
    var resTime = ""

    val windowTimeHours = totalMinutes / 60L
    if (windowTimeHours != 0L) {
        // *1 ?????? .. *2, *3, *4 ???????? .. *5, *6, *7, *8, *9, *0 ?????????? .. ????????. - 11 - 14
        val lastNumberOfHours = windowTimeHours % 10
        val endingHours = when {
            windowTimeHours in 11L..14L -> "????"
            lastNumberOfHours == 1L -> ""
            lastNumberOfHours in 2L..4L -> "??"
            else -> "????"
        }
        resTime += "$windowTimeHours ??????$endingHours"
    }

    val windowTimeMinutes = totalMinutes % 60
    if (windowTimeMinutes != 0L) {
        // *1 ???????????? .. *2, *3, *4 ???????????? .. *5, *6, *7, *8, *9, *0 ?????????? .. ????????. - 11 - 14
        val lastNumberOfMinutes = windowTimeMinutes % 10
        val endingMinutes = when {
            windowTimeMinutes in 11L..14L -> ""
            lastNumberOfMinutes == 1L -> "??"
            lastNumberOfMinutes in 2L..4L -> "??"
            else -> ""
        }
        if (resTime.isNotEmpty()) {
            resTime += " "
        }
        resTime += "$windowTimeMinutes ??????????$endingMinutes"
    }

    return resTime
}