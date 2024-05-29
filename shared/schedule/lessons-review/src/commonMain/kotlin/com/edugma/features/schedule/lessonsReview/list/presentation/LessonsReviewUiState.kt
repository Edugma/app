package com.edugma.features.schedule.lessonsReview.list.presentation

import androidx.compose.runtime.Immutable
import com.edugma.core.api.model.LceUiState
import com.edugma.features.schedule.lessonsReview.list.domain.LessonReviewUiState

@Immutable
data class LessonsReviewUiState(
    val lceState: LceUiState = LceUiState.init(),
    val lessons: List<LessonReviewUiState> = emptyList(),
)
