package io.edugma.features.schedule.scheduleInfo.lessonInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.api.utils.format
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.divider.EdDivider
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.molecules.avatar.EdAvatar
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.core.designSystem.utils.ContentAlpha
import io.edugma.core.designSystem.utils.PrimaryContent
import io.edugma.core.designSystem.utils.WithContentAlpha
import io.edugma.core.designSystem.utils.rememberAsyncImagePainter
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.features.schedule.domain.model.group.Group
import io.edugma.features.schedule.domain.model.lesson.LessonDateTime
import io.edugma.features.schedule.domain.model.lesson.LessonInfo
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import io.edugma.features.schedule.domain.model.teacher.description
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun LessonInfoScreen(
    viewModel: LessonInfoViewModel = getViewModel(),
    lessonInfo: LessonInfo?,
) {
    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onLessonInfo(lessonInfo)
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
                LessonType(
                    type = state.lessonInfo?.lesson?.type?.title ?: "",
                )
                PrimaryContent {
                    LessonTitle(
                        title = state.lessonInfo?.lesson?.subject?.title ?: "",
                    )
                }
                SpacerHeight(height = 4.dp)
                state.lessonInfo?.dateTime?.let { LessonDateTime(lessonDateTime = it) }
                SpacerHeight(height = 16.dp)
            }
        }
        if (state.teachers.isNotEmpty()) {
            SpacerHeight(height = 10.dp)
            LessonTeachers(
                teachers = state.teachers,
                onItemClick = onTeacherClick,
            )
        }
        if (state.lessonInfo?.lesson?.places?.isNotEmpty() == true) {
            SpacerHeight(height = 10.dp)
            LessonPlaces(
                places = state.lessonInfo.lesson.places,
                onItemClick = onPlaceClick,
            )
        }
        if (state.lessonInfo?.lesson?.groups?.isNotEmpty() == true) {
            SpacerHeight(height = 10.dp)
            LessonGroups(
                groups = state.lessonInfo.lesson.groups,
                onItemClick = onGroupClick,
            )
        }
        SpacerHeight(height = 10.dp)
    }
}

@Composable
private fun LessonType(type: String) {
    WithContentAlpha(ContentAlpha.medium) {
        Text(
            text = type,
            style = EdTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        )
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

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun LessonDateTime(lessonDateTime: LessonDateTime) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        WithContentAlpha(ContentAlpha.medium) {
            val timeStart = lessonDateTime.time.start.format("HH:mm")
            val timeEnd = lessonDateTime.time.end.format("HH:mm")
            val startDate = lessonDateTime.startDate.format("d MMMM yyyy (EE)") // + "!!"
            EdLabel(
                text = "$timeStart - $timeEnd",
                style = EdTheme.typography.bodySmall,
                iconPainter = painterResource(EdIcons.ic_fluent_clock_16_regular),
                spacing = 3.dp,
                modifier = Modifier.padding(start = 16.dp, end = 8.dp),
            )
            EdLabel(
                text = startDate,
                style = EdTheme.typography.bodySmall,
                iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_16_regular),
                spacing = 3.dp,
                modifier = Modifier.padding(start = 8.dp, end = 16.dp),
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun LessonTeachers(
    teachers: List<TeacherInfo>,
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
            WithContentAlpha(ContentAlpha.medium) {
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
                    description = teacher.description,
                    onItemClick = { onItemClick(teacher.id) },
                )
                if (index != teachers.size - 1) {
                    EdDivider(Modifier.padding(start = 70.dp, end = 20.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun LessonPlaces(
    places: List<Place>,
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
            WithContentAlpha(ContentAlpha.medium) {
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
            SpacerHeight(8.dp)
            val text = "https://static-maps.yandex.ru/1.x/?l=map&pt=37.544180,55.833268,org&z=16&size=450,250"
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
            SpacerHeight(8.dp)
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun LessonGroups(
    groups: List<Group>,
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
            WithContentAlpha(ContentAlpha.medium) {
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
                    imageInitials = group.title
                        .split(' ')
                        .joinToString(separator = "") { it.take(1) },
                    title = group.title,
                    description = group.description,
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
                WithContentAlpha(alpha = ContentAlpha.medium) {
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
