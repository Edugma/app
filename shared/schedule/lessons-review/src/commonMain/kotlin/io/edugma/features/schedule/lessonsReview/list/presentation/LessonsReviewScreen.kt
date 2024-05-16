package io.edugma.features.schedule.lessonsReview.list.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edugma.shared.core.resources.generated.resources.Res
import edugma.shared.core.resources.generated.resources.*
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.edugma.core.api.utils.DateFormat
import io.edugma.core.api.utils.capitalized
import io.edugma.core.api.utils.format
import io.edugma.core.api.utils.formatDate
import io.edugma.core.api.utils.formatTime
import io.edugma.core.arch.mvi.viewmodel.rememberOnAction
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.NavigationBarSpacer
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.organism.lceScaffold.EdLceScaffold
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.designSystem.tokens.shapes.top
import io.edugma.core.designSystem.utils.SecondaryContent
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.schedule.domain.model.compact.CompactLessonEvent
import io.edugma.features.schedule.domain.model.rrule.Frequency
import io.edugma.features.schedule.lessonsReview.list.domain.LessonReviewEvent
import io.edugma.features.schedule.lessonsReview.list.domain.LessonReviewEventsByPeriod
import io.edugma.features.schedule.lessonsReview.list.domain.LessonReviewPeriod
import io.edugma.features.schedule.lessonsReview.list.domain.LessonReviewUiState

@Composable
fun LessonsReviewScreen(
    viewModel: LessonsReviewViewModel = getViewModel(),
) {
    val state by viewModel.stateFlow.collectAsState()

    FeatureScreen(
        statusBarPadding = false,
        navigationBarPadding = false,
    ) {
        LessonsReviewContent(
            state = state,
            onBackClick = viewModel::exit,
            onAction = viewModel.rememberOnAction(),
        )
    }
}

@Composable
fun LessonsReviewContent(
    state: LessonsReviewUiState,
    onBackClick: ClickListener,
    onAction: (LessonsReviewAction) -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        EdSurface(
            shape = EdTheme.shapes.large.bottom(),
        ) {
            EdTopAppBar(
                title = stringResource(Res.string.schedule_les_rev_lessons_review),
                onNavigationClick = onBackClick,
                windowInsets = WindowInsets.statusBars,
                colors = EdTopAppBarDefaults.transparent(),
            )
        }
        EdLceScaffold(
            lceState = state.lceState,
            onRefresh = { onAction(LessonsReviewAction.OnRefresh) },
            placeholder = { /* TODO */ },
        ) {
            LessonsReviewList(lessons = state.lessons)
        }
    }
}

@Composable
private fun LessonsReviewList(lessons: List<LessonReviewUiState>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lessons) { lessonTimesReview ->
            LessonTimesReviewContent(lessonTimesReview)
        }
        item {
            NavigationBarSpacer(10.dp)
        }
    }
}

@Composable
fun LessonTimesReviewContent(lessonReviewUiState: LessonReviewUiState) {
    EdSurface(
        shape = EdTheme.shapes.large,
    ) {
        Column(
            Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
        ) {
            EdLabel(
                text = lessonReviewUiState.subject.title,
                style = EdTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            )
            SpacerHeight(8.dp)
            Column(
                Modifier.fillMaxWidth()
                    .padding(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                lessonReviewUiState.events.forEachIndexed { _, item ->
                    EventGroup(
                        eventsByPeriod = item,
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.EventGroup(eventsByPeriod: LessonReviewEventsByPeriod) {
    val groupTitle: String = when (val period = eventsByPeriod.period) {
        LessonReviewPeriod.OneTime -> "Один раз"
        is LessonReviewPeriod.Repeated -> when (period.frequency) {
            Frequency.Daily -> {
                if (period.interval == 1) {
                    "Каждый день"
                } else {
                    "Каждый ${period.interval}-й день"
                }
            }
            Frequency.Weekly -> {
                if (period.interval == 1) {
                    "Каждую неделю"
                } else {
                    "Каждую ${period.interval}-ю неделю"
                }
            }
            Frequency.Monthly -> {
                if (period.interval == 1) {
                    "Каждый месяц"
                } else {
                    "Каждый ${period.interval}-й месяц"
                }
            }
            Frequency.Yearly -> {
                if (period.interval == 1) {
                    "Каждый год"
                } else {
                    "Каждый ${period.interval}-й год"
                }
            }
        }
    }
    SecondaryContent {
        EdLabel(
            text = groupTitle.uppercase(),
            modifier = Modifier.padding(horizontal = 12.dp),
            style = EdTheme.typography.labelMedium,
        )
    }
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
    ) {
        items(eventsByPeriod.events) { event ->
            EventContent(event)
        }
    }
}

@Composable
private fun EventContent(
    event: LessonReviewEvent,
    modifier: Modifier = Modifier,
) {

    when (event) {
        is LessonReviewEvent.OneTime -> {
            OneTimeEventContent(
                event = event,
                modifier = modifier,
            )
        }
        is LessonReviewEvent.Repeated -> {
            RepeatedEventContent(
                event = event,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun OneTimeEventContent(
    event: LessonReviewEvent.OneTime,
    modifier: Modifier = Modifier,
) {
    val lessonEvent: CompactLessonEvent = event.event
    EdSurface(
        modifier = modifier.fillMaxHeight(),
        elevation = EdElevation.Level2,
        shape = EdTheme.shapes.medium,
    ) {
        val isOneDay = lessonEvent.start.dateTime.date == lessonEvent.end.dateTime.date
        Column(Modifier.padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 12.dp)) {
            if (isOneDay) {
                val startTime = lessonEvent.start.dateTime.formatTime()
                val endTime = lessonEvent.end.dateTime.formatTime()
                val time = "$startTime - $endTime"
                EdLabel(
                    iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_16_regular),
                    text = lessonEvent.start.dateTime.formatDate(
                        DateFormat.WEEK_DAY_MONTH_YEAR.hideYear(),
                    ).capitalized(),
                )
                EdLabel(
                    iconPainter = painterResource(EdIcons.ic_fluent_clock_16_regular),
                    text = time,
                )
            } else {
                // TODO проверить
                EdLabel(
                    text = lessonEvent.start.dateTime.format(),
                )
                EdLabel(
                    text = lessonEvent.end.dateTime.format(),
                )
            }
        }
    }
}

@Composable
private fun RepeatedEventContent(
    event: LessonReviewEvent.Repeated,
    modifier: Modifier = Modifier,
) {
    val lessonEvent: CompactLessonEvent = event.event
    val rRule = event.rrule
    EdSurface(
        modifier = modifier.fillMaxHeight(),
        elevation = EdElevation.Level2,
        shape = EdTheme.shapes.medium,
    ) {
        val isOneDay = lessonEvent.start.dateTime.date == lessonEvent.end.dateTime.date
        Column(Modifier.padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 12.dp)) {
            if (isOneDay) {
                val startTime = lessonEvent.start.dateTime.formatTime()
                val endTime = lessonEvent.end.dateTime.formatTime()
                val time = "$startTime - $endTime"
                val until = rRule.until
                val untilDate = if (until == null) {
                    null
                } else if (until.time == null) {
                    until.date.format(
                        DateFormat.FULL.hideYear(),
                    )
                } else {
                    until.toLocalDateTime()!!.format()
                }

                val stateDate = lessonEvent.start.dateTime.formatDate(
                    DateFormat.FULL.hideYear(),
                ).capitalized()
                val dateText = if (untilDate != null) {
                    "C $stateDate до $untilDate"
                } else {
                    "C $stateDate"
                }

                EdLabel(
                    iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_16_regular),
                    text = dateText,
                )

                EdLabel(
                    iconPainter = painterResource(EdIcons.ic_fluent_clock_16_regular),
                    text = time,
                )
            } else {
                // TODO проверить
                EdLabel(
                    text = lessonEvent.start.dateTime.format(),
                )
                EdLabel(
                    text = lessonEvent.end.dateTime.format(),
                )
            }
        }
    }
}

@Composable
private fun OtherEventContent(
    event: LessonReviewEvent.Repeated,
    modifier: Modifier = Modifier,
) {
    val lessonEvent: CompactLessonEvent = event.event
    val rRule = event.rrule
    EdSurface(
        modifier = modifier.fillMaxHeight(),
        elevation = EdElevation.Level2,
        shape = EdTheme.shapes.medium,
    ) {
        Column(Modifier.padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 12.dp)) {
            EdLabel(
                text = lessonEvent.start.dateTime.format(),
            )
            EdLabel(
                text = lessonEvent.end.dateTime.format(),
            )
            SpacerHeight(10.dp)
            EdLabel(
                text = rRule.toString(),
                style = EdTheme.typography.bodySmall,
            )
        }
    }
}
