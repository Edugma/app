package io.edugma.features.schedule.domain.repository

import io.edugma.domain.schedule.model.review.LessonTimesReview
import io.edugma.domain.schedule.model.source.ScheduleSource
import kotlinx.coroutines.flow.Flow

interface LessonsReviewRepository {
    fun getLessonsReview(source: ScheduleSource): Flow<Result<List<LessonTimesReview>>>
}
