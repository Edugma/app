package com.edugma.features.schedule.lessonsReview.list.presentation

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
import com.edugma.core.api.utils.getShortName
import com.edugma.core.designSystem.atoms.spacer.SpacerWidth
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.utils.edPlaceholder
import com.edugma.core.icons.EdIcons
import com.edugma.features.schedule.domain.model.attentdee.AttendeeInfo
import com.edugma.features.schedule.domain.model.lessonSubject.LessonSubject
import com.edugma.features.schedule.domain.model.place.Place
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

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
fun TeachersContent(teachers: List<AttendeeInfo>, isLoading: Boolean = false) {
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
                teachers.joinToString { getShortName(it.name) }
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
fun GroupsContent(groups: List<AttendeeInfo>, isLoading: Boolean = false) {
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
        val groupsText = remember(groups) { groups.joinToString { it.name } }
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
