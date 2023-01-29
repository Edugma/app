package io.edugma.features.schedule.history.changes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.withAlpha
import io.edugma.features.schedule.domain.model.group.Group
import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import io.edugma.features.schedule.domain.model.lesson_subject.LessonSubject
import io.edugma.features.schedule.domain.model.lesson_type.LessonType
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.model.teacher.Teacher
import io.edugma.features.schedule.domain.usecase.LessonChange
import kotlinx.datetime.Instant
import org.koin.androidx.compose.getViewModel
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleChangesScreen(
    viewModel: ScheduleChangesViewModel = getViewModel(),
    first: Instant,
    second: Instant,
) {
    val state by viewModel.state.collectAsState()

    ScheduleChangesContent(
        state = state,
        onBackClick = viewModel::exit,
    )
}

private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM, yyyy")

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScheduleChangesContent(
    state: ScheduleChangesState,
    onBackClick: ClickListener,
) {
    Column(
        Modifier
            .fillMaxSize(),
    ) {
        EdTopAppBar(
            title = "Изменения",
            onNavigationClick = onBackClick,
        )

        LazyColumn(Modifier.fillMaxSize()) {
            state.changes.forEach { (date, lessonsByDay) ->
                stickyHeader {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Surface(
                            shape = EdTheme.shapes.small,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                            color = EdTheme.colorScheme.background
                                .withAlpha(0.8f),
                        ) {
                            Text(
                                date.format(dateFormatter),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                style = EdTheme.typography.bodyMedium,
                            )
                        }
                    }
                }

                lessonsByDay.forEach { (time, lessonsByTime) ->
                    item {
                        LessonTime(time = time)
                    }
                    items(lessonsByTime) {
                        LessonChangeContent(
                            change = it,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonTime(time: LessonTime) {
    Text(
        "${time.start} - ${time.end}",
        modifier = Modifier.padding(start = 16.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonChangeContent(change: LessonChange) {
    val containerColor = when (change) {
        is LessonChange.Added -> EdTheme.customColorScheme.successContainer
        is LessonChange.Modified -> EdTheme.colorScheme.surfaceVariant
        is LessonChange.Removed -> EdTheme.colorScheme.errorContainer
    }

    var old: Lesson? = null
    var new: Lesson? = null

    when (change) {
        is LessonChange.Added -> {
            new = change.new
        }
        is LessonChange.Removed -> {
            old = change.old
        }
        is LessonChange.Modified -> {
            old = change.old
            new = change.new
        }
    }

    Card(
        Modifier
            .padding(horizontal = 8.dp, vertical = 5.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Column(
            Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .fillMaxWidth(),
        ) {
            when {
                old != null && new != null -> {
                    if (old.type != new.type) {
                        ChangedLessonType(old.type, isNew = false)
                        ChangedLessonType(new.type, isNew = true)
                    } else {
                        ChangedLessonType(new.type)
                    }

                    if (old.subject != new.subject) {
                        ChangedLessonSubject(old.subject, isNew = false)
                        ChangedLessonSubject(new.subject, isNew = true)
                    } else {
                        ChangedLessonSubject(new.subject)
                    }

                    if (old.teachers != new.teachers) {
                        ChangedTeachers(old.teachers, isNew = false)
                        ChangedTeachers(new.teachers, isNew = true)
                    } else {
                        ChangedTeachers(new.teachers)
                    }

                    if (old.groups != new.groups) {
                        ChangedGroups(old.groups, isNew = false)
                        ChangedGroups(new.groups, isNew = true)
                    } else {
                        ChangedGroups(new.groups)
                    }

                    if (old.places != new.places) {
                        ChangedPlaces(old.places, isNew = false)
                        ChangedPlaces(new.places, isNew = true)
                    } else {
                        ChangedPlaces(new.places)
                    }
                }
                old != null -> {
                    ChangedLessonType(old.type)
                    ChangedLessonSubject(old.subject)
                    ChangedTeachers(old.teachers)
                    ChangedGroups(old.groups)
                    ChangedPlaces(old.places)
                }
                new != null -> {
                    ChangedLessonType(new.type)
                    ChangedLessonSubject(new.subject)
                    ChangedTeachers(new.teachers)
                    ChangedGroups(new.groups)
                    ChangedPlaces(new.places)
                }
            }
        }
    }
}

@Composable
private fun ChangedLessonType(type: LessonType, isNew: Boolean? = null) {
    val containerColor = getColor(isNew)

    Surface(
        color = containerColor,
    ) {
        Text(type.title)
    }
}

@Composable
private fun ChangedLessonSubject(subject: LessonSubject, isNew: Boolean? = null) {
    val containerColor = getColor(isNew)

    Surface(
        color = containerColor,
    ) {
        Text(subject.title)
    }
}

@Composable
private fun ChangedTeachers(teachers: List<Teacher>, isNew: Boolean? = null) {
    val containerColor = getColor(isNew)

    Surface(
        color = containerColor,
    ) {
        Text(teachers.joinToString { it.name })
    }
}

@Composable
private fun ChangedGroups(groups: List<Group>, isNew: Boolean? = null) {
    val containerColor = getColor(isNew)

    Surface(
        color = containerColor,
    ) {
        Text(groups.joinToString { it.title })
    }
}

@Composable
private fun ChangedPlaces(places: List<Place>, isNew: Boolean? = null) {
    val containerColor = getColor(isNew)

    Surface(
        color = containerColor,
    ) {
        Text(places.joinToString { it.title })
    }
}

@Composable
private fun getColor(isNew: Boolean?): Color {
    return when (isNew) {
        true -> EdTheme.customColorScheme.successContainer
        false -> EdTheme.colorScheme.errorContainer
        null -> Color.Transparent
    }
}
