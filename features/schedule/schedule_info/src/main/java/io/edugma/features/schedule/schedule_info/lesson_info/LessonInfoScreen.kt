package io.edugma.features.schedule.schedule_info.lesson_info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.molecules.avatar.EdAvatar
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.utils.*
import io.edugma.features.schedule.domain.model.group.Group
import io.edugma.features.schedule.domain.model.lesson.LessonDateTime
import io.edugma.features.schedule.domain.model.lesson.LessonInfo
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import io.edugma.features.schedule.domain.model.teacher.description
import org.koin.androidx.compose.getViewModel
import java.time.format.DateTimeFormatter

@Composable
fun LessonInfoScreen(
    viewModel: LessonInfoViewModel = getViewModel(),
    lessonInfo: LessonInfo?,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onLessonInfo(lessonInfo)
    }

    LessonInfoContent(
        state = state,
        onBackClick = viewModel::exit,
        onTeacherClick = viewModel::onTeacherClick,
        onGroupClick = viewModel::onGroupClick,
        onPlaceClick = viewModel::onPlaceClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
        Surface(
            color = EdTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp),
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
                HighAlpha {
                    LessonTitle(
                        title = state.lessonInfo?.lesson?.subject?.title ?: "",
                    )
                }
                SpacerHeight(height = 4.dp)
                state.lessonInfo?.dateTime?.let { LessonDateTime(lessonDateTime = it) }
                SpacerHeight(height = 16.dp)
            }
        }
        SpacerHeight(height = 8.dp)
        LessonTeachers(
            teachers = state.teachers,
            onItemClick = onTeacherClick,
        )
        SpacerHeight(height = 14.dp)
        LessonPlaces(
            places = state.lessonInfo?.lesson?.places ?: emptyList(),
            onItemClick = onPlaceClick,
        )
        SpacerHeight(height = 14.dp)
        LessonGroups(
            groups = state.lessonInfo?.lesson?.groups ?: emptyList(),
            onItemClick = onGroupClick,
        )
        SpacerHeight(height = 14.dp)
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

private val lessonDateFormat = DateTimeFormatter.ofPattern("d MMMM yyyy (EE)")
private val lessonTimeFormat = DateTimeFormatter.ofPattern("HH:mm")

@Composable
private fun LessonDateTime(lessonDateTime: LessonDateTime) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        WithContentAlpha(ContentAlpha.medium) {
            val timeStart = lessonDateTime.time.start.format(lessonTimeFormat)
            val timeEnd = lessonDateTime.time.end.format(lessonTimeFormat)
            val startDate = lessonDateTime.startDate.format(lessonDateFormat) // + "!!"
            Row(
                Modifier.padding(start = 16.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(FluentIcons.ic_fluent_clock_16_regular),
                    contentDescription = null,
                )
                SpacerWidth(3.dp)
                Text(
                    text = "$timeStart - $timeEnd",
                    style = EdTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 1.dp),
                )
            }
            Row(
                Modifier.padding(start = 8.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(FluentIcons.ic_fluent_calendar_ltr_16_regular),
                    contentDescription = null,
                )
                SpacerWidth(3.dp)
                Text(
                    text = startDate,
                    style = EdTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 1.dp),
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.LessonTeachers(
    teachers: List<TeacherInfo>,
    onItemClick: Typed1Listener<String>,
) {
    WithContentAlpha(ContentAlpha.medium) {
        Row(
            Modifier.padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(FluentIcons.ic_fluent_hat_graduation_20_regular),
                contentDescription = null,
            )
            SpacerWidth(4.dp)
            Text(
                text = "Преподаватели",
                style = EdTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 2.dp),
            )
        }
    }
    SpacerHeight(1.dp)
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
            Divider(Modifier.padding(start = 70.dp, end = 20.dp))
        }
    }
}

@Composable
private fun ColumnScope.LessonPlaces(
    places: List<Place>,
    onItemClick: Typed1Listener<String>,
) {
    WithContentAlpha(ContentAlpha.medium) {
        Row(
            Modifier.padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(FluentIcons.ic_fluent_location_20_regular),
                contentDescription = null,
            )
            SpacerWidth(4.dp)
            Text(
                text = "Места",
                style = EdTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 2.dp),
            )
        }
    }
    SpacerHeight(1.dp)
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
            Divider(Modifier.padding(start = 70.dp, end = 20.dp))
        }
    }
}

@Composable
private fun ColumnScope.LessonGroups(
    groups: List<Group>,
    onItemClick: Typed1Listener<String>,
) {
    WithContentAlpha(ContentAlpha.medium) {
        Row(
            Modifier.padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(FluentIcons.ic_fluent_people_20_regular),
                contentDescription = null,
            )
            SpacerWidth(4.dp)
            Text(
                text = "Группы",
                style = EdTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 2.dp),
            )
        }
    }
    SpacerHeight(1.dp)
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
            Divider(Modifier.padding(start = 70.dp, end = 20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LessonItem(
    imageUrl: String,
    imageInitials: String = "",
    title: String,
    description: String,
    onItemClick: ClickListener,
) {
    Surface(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        onClick = onItemClick,
        shape = RoundedCornerShape(10.dp),
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
