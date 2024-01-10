package io.edugma.features.schedule.domain.repository

import io.edugma.core.api.utils.LceFlow
import io.edugma.features.schedule.domain.model.review.LessonTimesReview
import io.edugma.features.schedule.domain.model.source.ScheduleSource

interface LessonsReviewRepository {
    fun getLessonsReview(source: ScheduleSource): LceFlow<List<LessonTimesReview>>
}
