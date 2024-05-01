package io.edugma.features.schedule.history.presentation.changes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.edugma.core.api.utils.DateFormat
import io.edugma.core.api.utils.TimeFormat
import io.edugma.core.api.utils.formatDate
import io.edugma.core.api.utils.formatTime
import io.edugma.core.arch.viewmodel.bind
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.designSystem.tokens.shapes.top
import io.edugma.core.designSystem.utils.SecondaryContent
import io.edugma.core.icons.EdIcons
import io.edugma.core.navigation.schedule.ScheduleHistoryScreens
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.schedule.domain.model.attentdee.AttendeeInfo
import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import io.edugma.features.schedule.domain.model.lessonSubject.LessonSubject
import io.edugma.features.schedule.domain.model.lessonType.LessonType
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.usecase.LessonChange
import io.edugma.navigation.core.screen.NavArgs
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScheduleChangesScreen(
    viewModel: ScheduleChangesViewModel = getViewModel(),
    args: NavArgs<ScheduleHistoryScreens.Changes>,
) {
    val state by viewModel.stateFlow.collectAsState()
    viewModel.bind {
        viewModel.onAction(
            ScheduleChangesAction.OnArguments(
                args = args,
            ),
        )
    }

    FeatureScreen(
        statusBarPadding = false,
        navigationBarPadding = false,
    ) {
        ScheduleChangesContent(
            state = state,
            onBackClick = viewModel::exit,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleChangesContent(
    state: ScheduleChangesUiState,
    onBackClick: ClickListener,
) {
    Column(
        Modifier.fillMaxSize(),
    ) {
        EdSurface(
            shape = EdTheme.shapes.large.bottom(),
        ) {
            Column(Modifier.fillMaxWidth()) {
                EdTopAppBar(
                    title = "Изменения",
                    onNavigationClick = onBackClick,
                    modifier = Modifier.statusBarsPadding(),
                )
                if (state.firstSelected != null && state.secondSelected != null) {
                    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        EdSurface(
                            shape = EdTheme.shapes.medium,
                            modifier = Modifier.weight(1f),
                        ) {
                            Column(Modifier.padding(vertical = 8.dp, horizontal = 12.dp)) {
                                EdLabel(
                                    text = "Предыдущее",
                                    style = EdTheme.typography.labelLarge,
                                )
                                SpacerHeight(4.dp)
                                SecondaryContent {
                                    val date1 = remember(state.firstSelected) {
                                        state.firstSelected.toLocalDateTime(TimeZone.currentSystemDefault())
                                    }
                                    EdLabel(
                                        text = date1.formatDate(DateFormat.FULL),
                                        iconPainter = painterResource(
                                            EdIcons.ic_fluent_calendar_ltr_16_regular,
                                        ),
                                        style = EdTheme.typography.bodySmall,
                                    )
                                    EdLabel(
                                        text = date1.formatTime(TimeFormat.HOURS_MINUTES),
                                        iconPainter = painterResource(
                                            EdIcons.ic_fluent_clock_16_regular,
                                        ),
                                        style = EdTheme.typography.bodySmall,
                                    )
                                }
                            }
                        }
                        SpacerWidth(8.dp)
                        EdSurface(
                            shape = EdTheme.shapes.medium,
                            modifier = Modifier.weight(1f),
                        ) {
                            Column(Modifier.padding(vertical = 8.dp, horizontal = 12.dp)) {
                                EdLabel(
                                    text = "Следующее",
                                    style = EdTheme.typography.labelLarge,
                                )
                                SpacerHeight(4.dp)
                                SecondaryContent {
                                    val date2 = remember(state.secondSelected) {
                                        state.secondSelected.toLocalDateTime(TimeZone.currentSystemDefault())
                                    }
                                    EdLabel(
                                        text = date2.formatDate(DateFormat.FULL),
                                        iconPainter = painterResource(
                                            EdIcons.ic_fluent_calendar_ltr_16_regular,
                                        ),
                                        style = EdTheme.typography.bodySmall,
                                    )
                                    EdLabel(
                                        text = date2.formatTime(TimeFormat.HOURS_MINUTES),
                                        iconPainter = painterResource(
                                            EdIcons.ic_fluent_clock_16_regular,
                                        ),
                                        style = EdTheme.typography.bodySmall,
                                    )
                                }
                            }
                        }
                    }
                    SpacerHeight(8.dp)
                }
            }
        }
        SpacerHeight(10.dp)
        EdSurface(
            shape = EdTheme.shapes.large.top(),
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(state.changes) {
                    LessonChangeContent(
                        change = it,
                    )
                }
                item {
                    Spacer(Modifier.navigationBarsPadding())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonChangeContent(change: LessonChange) {
    val containerColor = when (change) {
        is LessonChange.Added -> EdTheme.customColorScheme.successContainer
        is LessonChange.Modified -> EdTheme.colorScheme.surfaceVariant
        is LessonChange.Removed -> EdTheme.colorScheme.errorContainer
    }

    var old: LessonEvent? = null
    var new: LessonEvent? = null

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
                    // TODO
//                    if (old.type != new.type) {
//                        ChangedLessonType(old.type, isNew = false)
//                        ChangedLessonType(new.type, isNew = true)
//                    } else {
//                        ChangedLessonType(new.type)
//                    }

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
                    // TODO
                    // ChangedLessonType(old.type)
                    ChangedLessonSubject(old.subject)
                    ChangedTeachers(old.teachers)
                    ChangedGroups(old.groups)
                    ChangedPlaces(old.places)
                }
                new != null -> {
                    // TODO
                    // ChangedLessonType(new.type)
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
private fun ChangedTeachers(teachers: List<AttendeeInfo>, isNew: Boolean? = null) {
    val containerColor = getColor(isNew)

    Surface(
        color = containerColor,
    ) {
        Text(teachers.joinToString { it.name })
    }
}

@Composable
private fun ChangedGroups(groups: List<AttendeeInfo>, isNew: Boolean? = null) {
    val containerColor = getColor(isNew)

    Surface(
        color = containerColor,
    ) {
        Text(groups.joinToString { it.name })
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
