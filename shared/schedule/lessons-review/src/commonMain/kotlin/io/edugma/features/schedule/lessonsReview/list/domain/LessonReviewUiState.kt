package io.edugma.features.schedule.lessonsReview.list.domain

import androidx.compose.runtime.Immutable
import io.edugma.features.schedule.domain.model.compact.CompactLessonSubjectInfo

@Immutable
data class LessonReviewUiState(
    val subject: CompactLessonSubjectInfo,
    val events: List<LessonReviewEventsByPeriod>,
)
