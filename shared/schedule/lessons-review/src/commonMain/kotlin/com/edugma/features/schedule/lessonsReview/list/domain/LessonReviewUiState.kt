package com.edugma.features.schedule.lessonsReview.list.domain

import androidx.compose.runtime.Immutable
import com.edugma.features.schedule.domain.model.compact.CompactLessonSubjectInfo

@Immutable
data class LessonReviewUiState(
    val subject: CompactLessonSubjectInfo,
    val events: List<LessonReviewEventsByPeriod>,
)
