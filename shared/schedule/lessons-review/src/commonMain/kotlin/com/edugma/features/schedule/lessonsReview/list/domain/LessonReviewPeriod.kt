package com.edugma.features.schedule.lessonsReview.list.domain

import androidx.compose.runtime.Immutable
import com.edugma.features.schedule.domain.model.rrule.Frequency

@Immutable
sealed interface LessonReviewPeriod {
    data object OneTime : LessonReviewPeriod

    data class Repeated(
        val frequency: Frequency,
        val interval: Int,
    ) : LessonReviewPeriod
}
