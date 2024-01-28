package io.edugma.features.schedule.lessonsReview.list.domain

import androidx.compose.runtime.Immutable

@Immutable
data class LessonReviewEventsByPeriod(
    val period: LessonReviewPeriod,
    val events: List<LessonReviewEvent>,
)
