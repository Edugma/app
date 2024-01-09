package io.edugma.features.schedule.lessonsReview

import androidx.compose.runtime.Immutable
import io.edugma.core.api.model.LceUiState
import io.edugma.features.schedule.domain.model.review.LessonTimesReview

@Immutable
data class LessonsReviewUiState(
    val lceState: LceUiState = LceUiState.init(),
    val lessons: List<LessonTimesReview> = emptyList(),
)
