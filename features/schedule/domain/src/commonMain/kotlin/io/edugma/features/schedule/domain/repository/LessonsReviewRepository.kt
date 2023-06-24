package io.edugma.features.schedule.domain.repository

import io.edugma.features.schedule.domain.model.review.LessonTimesReview
import io.edugma.features.schedule.domain.model.source.ScheduleSource

interface LessonsReviewRepository {
    suspend fun getLessonsReview(source: ScheduleSource): List<LessonTimesReview>
}
