package com.edugma.features.schedule.scheduleInfo.lessonInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.edugma.core.api.model.Coordinates
import com.edugma.core.designSystem.atoms.divider.EdDivider
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.atoms.spacer.SpacerWidth
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.molecules.avatar.EdAvatar
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.elevation.EdElevation
import com.edugma.core.designSystem.utils.PrimaryContent
import com.edugma.core.designSystem.utils.SecondaryContent
import com.edugma.core.designSystem.utils.rememberAsyncImagePainter
import com.edugma.core.icons.EdIcons
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.ClickListener
import com.edugma.core.utils.Typed1Listener
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel
import com.edugma.features.schedule.domain.model.attentdee.AttendeeInfo
import com.edugma.features.schedule.domain.model.place.Place
import edugma.shared.core.icons.generated.resources.*
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource

@Composable
fun LessonInfoScreen(
    viewModel: LessonInfoViewModel = getViewModel(),
    eventId: String,
    currentDate: LocalDate,
) {
    val state by viewModel.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onLessonInfo(
            eventId = eventId,
            currentDate = currentDate
        )
    }

    FeatureScreen(
        statusBarPadding = false,
    ) {
        LessonInfoContent(
            state = state,
            onBackClick = viewModel::exit,
            onTeacherClick = viewModel::onTeacherClick,
            onGroupClick = viewModel::onGroupClick,
            onPlaceClick = viewModel::onPlaceClick,
        )
    }
}

@Composable
private fun LessonInfoContent(
    state: LessonInfoState,
    onBackClick: ClickListener,
    onTeacherClick: Typed1Listener<String>,
    onGroupClick: Typed1Listener<String>,
    onPlaceClick: Typed1Listener<String>,
) {
    // TODO
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        EdSurface(
            shape = EdTheme.shapes.large,
        ) {
            Column(Modifier.fillMaxWidth()) {
                EdTopAppBar(
                    title = "",
                    onNavigationClick = onBackClick,
                    colors = EdTopAppBarDefaults.colors(containerColor = Color.Transparent),
                    windowInsets = WindowInsets.statusBars,
                )
                SpacerHeight(height = 32.dp)
                state.lessonInfo?.tags?.let { tags ->
                    LessonTags(
                        tags = tags,
                    )
                }
                PrimaryContent {
                    LessonTitle(
                        title = state.lessonInfo?.subject?.title ?: "",
                    )
                }
                SpacerHeight(height = 4.dp)
                //state.lessonInfo?.dateTime?.let { LessonDateTime(lessonDateTime = it) }
                SpacerHeight(height = 16.dp)
            }
        }
        if (state.lessonInfo?.teachers?.isNotEmpty() == true) {
            SpacerHeight(height = 10.dp)
            LessonTeachers(
                teachers = state.lessonInfo.teachers,
                onItemClick = onTeacherClick,
            )
        }
        if (state.lessonInfo?.places?.isNotEmpty() == true) {
            SpacerHeight(height = 10.dp)
            LessonPlaces(
                places = state.lessonInfo.places,
                coordinates = state.coordinates,
                onItemClick = onPlaceClick,
            )
        }
        if (state.lessonInfo?.groups?.isNotEmpty() == true) {
            SpacerHeight(height = 10.dp)
            LessonGroups(
                groups = state.lessonInfo.groups,
                onItemClick = onGroupClick,
            )
        }
        SpacerHeight(height = 10.dp)
    }
}

@Composable
private fun LessonTags(tags: List<String>) {
    SecondaryContent {
        Row(Modifier.fillMaxWidth()) {
            // TODO
            tags.forEach { type ->
                Text(
                    text = type,
                    style = EdTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun LessonTitle(title: String) {
    Text(
        text = title,
        style = EdTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
    )
}

// @Composable
// private fun LessonDateTime(lessonDateTime: LessonEvent) {
//    Row(
//        Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//    ) {
//        SecondaryContent {
//            val timeStart = lessonDateTime.time.start.format(TimeFormat.HOURS_MINUTES)
//            val timeEnd = lessonDateTime.time.end.format(TimeFormat.HOURS_MINUTES)
//            val startDate = lessonDateTime.startDate.format("d MMMM yyyy (EE)") // + "!!"
//            EdLabel(
//                text = "$timeStart - $timeEnd",
//                style = EdTheme.typography.bodySmall,
//                iconPainter = painterResource(EdIcons.ic_fluent_clock_16_regular),
//                spacing = 3.dp,
//                modifier = Modifier.padding(start = 16.dp, end = 8.dp),
//            )
//            EdLabel(
//                text = startDate,
//                style = EdTheme.typography.bodySmall,
//                iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_16_regular),
//                spacing = 3.dp,
//                modifier = Modifier.padding(start = 8.dp, end = 16.dp),
//            )
//        }
//    }
// }

@Composable
private fun LessonTeachers(
    teachers: List<AttendeeInfo>,
    onItemClick: Typed1Listener<String>,
) {
    EdSurface(
        shape = EdTheme.shapes.large,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            SecondaryContent {
                EdLabel(
                    text = "Преподаватели",
                    iconPainter = painterResource(EdIcons.ic_fluent_hat_graduation_24_filled),
                    spacing = 8.dp,
                    style = EdTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    iconStart = true,
                )
            }
            SpacerHeight(8.dp)
            teachers.forEachIndexed { index, teacher ->
                LessonItem(
                    imageUrl = "",
                    imageInitials = teacher.name
                        .split(' ')
                        .joinToString(separator = "") { it.take(1) },
                    title = teacher.name,
                    description = teacher.description.orEmpty(),
                    onItemClick = { onItemClick(teacher.id) },
                )
                if (index != teachers.size - 1) {
                    EdDivider(Modifier.padding(start = 70.dp, end = 20.dp))
                }
            }
        }
    }
}

@Composable
private fun LessonPlaces(
    places: List<Place>,
    coordinates: Coordinates?,
    onItemClick: Typed1Listener<String>,
) {
    EdSurface(
        shape = EdTheme.shapes.large,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            SecondaryContent {
                EdLabel(
                    text = "Места",
                    iconPainter = painterResource(EdIcons.ic_fluent_location_24_filled),
                    spacing = 8.dp,
                    style = EdTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    iconStart = true,
                )
            }
            SpacerHeight(8.dp)
            places.forEachIndexed { index, place ->
                LessonItem(
                    imageUrl = "",
                    imageInitials = place.title
                        .split(' ')
                        .joinToString(separator = "") { it.take(1) },
                    title = place.title,
                    description = place.description,
                    onItemClick = { onItemClick(place.id) },
                )
                if (index != places.size - 1) {
                    EdDivider(Modifier.padding(start = 70.dp, end = 20.dp))
                }
            }
            // TODO fix map for all places
            if (coordinates != null) {
                val x = coordinates.lng
                val y = coordinates.lat
                SpacerHeight(8.dp)
                val text = "https://static-maps.yandex.ru/1.x/?l=map&pt=$x,$y,org&z=16&size=450,250"
                val painter = rememberAsyncImagePainter(model = text)
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                        .clip(EdTheme.shapes.large),
                    contentScale = ContentScale.FillWidth,
                )
            }
            SpacerHeight(8.dp)
        }
    }
}

@Composable
private fun LessonGroups(
    groups: List<AttendeeInfo>,
    onItemClick: Typed1Listener<String>,
) {
    EdSurface(
        shape = EdTheme.shapes.large,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            SecondaryContent {
                EdLabel(
                    text = "Группы",
                    iconPainter = painterResource(EdIcons.ic_fluent_people_24_filled),
                    spacing = 8.dp,
                    style = EdTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    iconStart = true,
                )
            }
            SpacerHeight(8.dp)
            groups.forEachIndexed { index, group ->
                LessonItem(
                    imageUrl = "",
                    imageInitials = group.name
                        .split(' ')
                        .joinToString(separator = "") { it.take(1) },
                    title = group.name,
                    description = group.description.orEmpty(),
                    onItemClick = { onItemClick(group.id) },
                )
                if (index != groups.size - 1) {
                    EdDivider(Modifier.padding(start = 70.dp, end = 20.dp))
                }
            }
        }
    }
}

@Composable
private fun LessonItem(
    imageUrl: String,
    imageInitials: String = "",
    title: String,
    description: String,
    onItemClick: ClickListener,
) {
    EdSurface(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        onClick = onItemClick,
        shape = EdTheme.shapes.large,
        elevation = EdElevation.Level0,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EdAvatar(
                url = imageUrl,
                initials = imageInitials,
            )
            SpacerWidth(8.dp)
            Column(Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = title,
                    style = EdTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                SecondaryContent {
                    Text(
                        text = description,
                        style = EdTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
