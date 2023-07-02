package io.edugma.features.schedule.lessonsReview

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.edPlaceholder
import io.edugma.core.icons.EdIcons
import io.edugma.features.schedule.domain.model.group.Group
import io.edugma.features.schedule.domain.model.lessonSubject.LessonSubject
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.model.teacher.Teacher
import io.edugma.features.schedule.domain.usecase.getShortName
import org.jetbrains.compose.resources.ExperimentalResourceApi
import dev.icerock.moko.resources.compose.painterResource

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
    Row(
        modifier = Modifier.edPlaceholder(visible = isLoading),
    ) {
        Icon(
            painter = painterResource(EdIcons.ic_fluent_hat_graduation_16_regular),
            contentDescription = null,
            modifier = Modifier
                .size(17.dp)
                .align(Alignment.CenterVertically),
        )
        SpacerWidth(width = 5.dp)
        val teachersText = remember(teachers) {
            if (teachers.size == 1) {
                teachers.first().name
            } else {
                teachers.joinToString { it.getShortName() }
            }
        }
        Text(
            text = teachersText,
            style = EdTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GroupsContent(groups: List<Group>, isLoading: Boolean = false) {
    Row(
        modifier = Modifier.edPlaceholder(visible = isLoading),
    ) {
        Icon(
            painter = painterResource(EdIcons.ic_fluent_people_16_regular),
            contentDescription = null,
            modifier = Modifier
                .size(17.dp)
                .align(Alignment.CenterVertically),
        )
        SpacerWidth(width = 5.dp)
        val groupsText = remember(groups) { groups.joinToString { it.title } }
        Text(
            text = groupsText,
            style = EdTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PlacesContent(places: List<Place>, isLoading: Boolean = false) {
    Row(
        modifier = Modifier.edPlaceholder(visible = isLoading),
    ) {
        Icon(
            painter = painterResource(EdIcons.ic_fluent_location_16_regular),
            contentDescription = null,
            modifier = Modifier
                .size(17.dp)
                .align(Alignment.CenterVertically),
        )
        SpacerWidth(width = 5.dp)
        val placesText = remember(places) { places.joinToString { it.title } }
        Text(
            text = placesText,
            style = EdTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
        )
    }
}
