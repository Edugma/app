package io.edugma.features.schedule.lessons_review

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.domain.schedule.model.group.Group
import io.edugma.domain.schedule.model.lesson_subject.LessonSubject
import io.edugma.domain.schedule.model.place.Place
import io.edugma.domain.schedule.model.teacher.Teacher
import io.edugma.domain.schedule.utils.getShortName
import io.edugma.features.base.core.utils.FluentIcons
import io.edugma.features.base.elements.placeholder


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
