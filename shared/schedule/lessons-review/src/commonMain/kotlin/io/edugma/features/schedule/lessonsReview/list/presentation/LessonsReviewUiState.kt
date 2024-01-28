package io.edugma.features.schedule.lessonsReview.list.presentation

import androidx.compose.runtime.Immutable
import io.edugma.core.api.model.LceUiState
import io.edugma.features.schedule.lessonsReview.list.domain.LessonReviewUiState

@Immutable
data class LessonsReviewUiState(
    val lceState: LceUiState = LceUiState.init(),
    val lessons: List<LessonReviewUiState> = emptyList(),
)
