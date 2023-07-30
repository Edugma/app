package io.edugma.features.schedule.elements.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.api.utils.format
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.core.designSystem.utils.ContentAlpha
import io.edugma.core.designSystem.utils.WithContentAlpha
import io.edugma.core.designSystem.utils.edPlaceholder
import io.edugma.core.designSystem.utils.rememberAsyncImagePainter
import io.edugma.core.icons.EdIcons
import io.edugma.core.utils.Typed1Listener
import io.edugma.features.schedule.domain.model.group.Group
import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.lessonSubject.LessonSubject
import io.edugma.features.schedule.domain.model.lessonType.LessonType
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.model.teacher.Teacher
import io.edugma.features.schedule.domain.usecase.getShortName
import io.edugma.features.schedule.elements.lesson.model.ScheduleItem
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun LessonContent(
    lesson: Lesson,
    displaySettings: LessonDisplaySettings,
    onLessonClick: Typed1Listener<Lesson>,
) {
    EdCard(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .fillMaxWidth(),
        elevation = EdElevation.Level2,
        onClick = { onLessonClick(lesson) },
        shape = EdTheme.shapes.large,
    ) {
        Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 13.dp, bottom = 16.dp)) {
            WithContentAlpha(ContentAlpha.medium) {
                LessonHeader(lesson.type)
            }
            SpacerHeight(3.dp)
            WithContentAlpha(ContentAlpha.high) {
                LessonTitle(lesson.subject)
            }
            SpacerHeight(4.dp)
            WithContentAlpha(ContentAlpha.medium) {
                if (displaySettings.showTeachers && lesson.teachers.isNotEmpty()) {
                    SpacerHeight(2.dp)
                    TeachersContent(lesson.teachers)
                    SpacerHeight(2.dp)
                }
                if (displaySettings.showGroups && lesson.groups.isNotEmpty()) {
                    SpacerHeight(2.dp)
                    GroupsContent(lesson.groups)
                    SpacerHeight(2.dp)
                }
                if (displaySettings.showPlaces && lesson.places.isNotEmpty()) {
                    SpacerHeight(2.dp)
                    PlacesContent(lesson.places)
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
        EdTheme.colorScheme.error
    } else {
        Color.Unspecified
    }
    Text(
        text = type.title.uppercase(),
        style = EdTheme.typography.labelSmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.edPlaceholder(visible = isLoading),
        color = color,
    )
}

@Composable
fun LessonTitle(subject: LessonSubject, isLoading: Boolean = false) {
    Text(
        text = subject.title,
        style = EdTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .edPlaceholder(visible = isLoading),
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TeachersContent(teachers: List<Teacher>, isLoading: Boolean = false) {
    val teachersText = remember(teachers) {
        if (teachers.size == 1) {
            teachers.first().name
        } else {
            teachers.joinToString { it.getShortName() }
        }
    }
    EdLabel(
        text = teachersText,
        iconPainter = painterResource(EdIcons.ic_fluent_hat_graduation_16_filled),
        style = EdTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxWidth().edPlaceholder(visible = isLoading),
        spacing = 5.dp,
        iconSize = 17.dp,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GroupsContent(groups: List<Group>, isLoading: Boolean = false) {
    val groupsText = remember(groups) { groups.joinToString { it.title } }
    EdLabel(
        text = groupsText,
        iconPainter = painterResource(EdIcons.ic_fluent_people_16_filled),
        style = EdTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxWidth().edPlaceholder(visible = isLoading),
        spacing = 5.dp,
        iconSize = 17.dp,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PlacesContent(places: List<Place>, isLoading: Boolean = false) {
    val placesText = remember(places) { places.joinToString { it.title } }
    EdLabel(
        text = placesText,
        iconPainter = painterResource(EdIcons.ic_fluent_location_16_filled),
        style = EdTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxWidth().edPlaceholder(visible = isLoading),
        spacing = 5.dp,
        iconSize = 17.dp,
    )
}

@Composable
fun LessonWindow(lessonWindow: ScheduleItem.Window) {
    val timeFrom = remember(lessonWindow) { lessonWindow.timeFrom.format("HH:mm") }
    val timeTo = remember(lessonWindow) { lessonWindow.timeTo.format("HH:mm") }

    val timeText = remember(lessonWindow) {
        getTimeText(lessonWindow.totalMinutes)
    }

    Row(
        Modifier
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val urls = listOf(
            "https://img.icons8.com/fluency/200/cup.png",
            "https://img.icons8.com/fluency/192/kfc-chicken.png",
            "https://img.icons8.com/fluency/200/controller.png",
            "https://img.icons8.com/fluency/200/sunbathe.png",
        )
        val url = remember { urls.random() }

        Image(
            painter = rememberAsyncImagePainter(model = url),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(60.dp),
            contentScale = ContentScale.Inside,
            alignment = Alignment.Center,
        )
        Column(
            modifier = Modifier
                .padding(start = 14.dp, end = 12.dp, top = 11.dp, bottom = 14.dp),
        ) {
            Text(
                text = "Окно на $timeText",
                style = EdTheme.typography.titleMedium,
            )
            WithContentAlpha(alpha = ContentAlpha.medium) {
                Text(
                    text = "$timeFrom - $timeTo",
                    style = EdTheme.typography.bodyMedium,
                )
            }
        }
    }
}

private fun getTimeText(totalMinutes: Long): String {
    var resTime = ""

    val windowTimeHours = totalMinutes / 60L
    if (windowTimeHours != 0L) {
        // *1 час .. *2, *3, *4 часа .. *5, *6, *7, *8, *9, *0 часов .. искл. - 11 - 14
        val lastNumberOfHours = windowTimeHours % 10
        val endingHours = when {
            windowTimeHours in 11L..14L -> "ов"
            lastNumberOfHours == 1L -> ""
            lastNumberOfHours in 2L..4L -> "а"
            else -> "ов"
        }
        resTime += "$windowTimeHours час$endingHours"
    }

    val windowTimeMinutes = totalMinutes % 60
    if (windowTimeMinutes != 0L) {
        // *1 минута .. *2, *3, *4 минуты .. *5, *6, *7, *8, *9, *0 минут .. искл. - 11 - 14
        val lastNumberOfMinutes = windowTimeMinutes % 10
        val endingMinutes = when {
            windowTimeMinutes in 11L..14L -> ""
            lastNumberOfMinutes == 1L -> "а"
            lastNumberOfMinutes in 2L..4L -> "ы"
            else -> ""
        }
        if (resTime.isNotEmpty()) {
            resTime += " "
        }
        resTime += "$windowTimeMinutes минут$endingMinutes"
    }

    return resTime
}

@Composable
fun LessonPlaceholder() {
    EdCard(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .fillMaxWidth(),
        elevation = EdElevation.Level2,
        onClick = { },
        shape = EdTheme.shapes.large,
    ) {
        Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 13.dp, bottom = 16.dp)) {
            Box(
                Modifier.size(100.dp, 10.dp)
                    .edPlaceholder(),
            )
            SpacerHeight(3.dp)
            Box(
                Modifier.height(10.dp)
                    .fillMaxWidth()
                    .edPlaceholder(),
            )
            SpacerHeight(6.dp)
            Box(
                Modifier.height(10.dp)
                    .fillMaxWidth()
                    .edPlaceholder(),
            )
            SpacerHeight(4.dp)
            Box(
                Modifier.height(10.dp)
                    .fillMaxWidth()
                    .edPlaceholder(),
            )
            SpacerHeight(4.dp)
            Box(
                Modifier.height(10.dp)
                    .fillMaxWidth()
                    .edPlaceholder(),
            )
            SpacerHeight(2.dp)
        }
    }
}
