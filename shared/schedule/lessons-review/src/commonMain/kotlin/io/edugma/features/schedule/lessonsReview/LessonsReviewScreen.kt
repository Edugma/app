package io.edugma.features.schedule.lessonsReview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.api.utils.format
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
import io.edugma.core.resources.MR
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.schedule.domain.model.compact.CompactLessonEvent
import io.edugma.features.schedule.domain.model.review.LessonTimesReview

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
                title = stringResource(MR.strings.schedule_les_rev_lessons_review),
                onNavigationClick = onBackClick,
                windowInsets = WindowInsets.statusBars,
                colors = EdTopAppBarDefaults.transparent(),
            )
        }
        SpacerHeight(10.dp)
        EdSurface(
            shape = EdTheme.shapes.large.top(),
        ) {
            EdLceScaffold(
                lceState = state.lceState,
                onRefresh = { onAction(LessonsReviewAction.OnRefresh) },
                placeholder = { /* TODO */ },
            ) {
                LessonsReviewList(lessons = state.lessons)
            }
        }
    }
}

@Composable
private fun LessonsReviewList(lessons: List<LessonTimesReview>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(lessons) { lessonTimesReview ->
            LessonTimesReviewContent(lessonTimesReview)
        }
        item {
            NavigationBarSpacer(10.dp)
        }
    }
}

@Composable
fun LessonTimesReviewContent(lessonTimesReview: LessonTimesReview) {
    Column(
        Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = lessonTimesReview.subject.title,
            style = EdTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
        )
        SpacerHeight(2.dp)
        Column(
            Modifier.fillMaxWidth()
                .padding(),
        ) {
            lessonTimesReview.events.forEach { item ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .height(IntrinsicSize.Max),
                ) {
                    DatesAndTimeUnit(
                        item,
                        // modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DatesAndTimeUnit(
    lessonEvent: CompactLessonEvent,
    modifier: Modifier = Modifier,
) {
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
            lessonEvent.recurrence.forEach {
                EdLabel(
                    text = it.toString(),
                    style = EdTheme.typography.bodySmall,
                )
            }
        }
    }
}
