package io.edugma.data.schedule.repository

import io.edugma.core.api.utils.LceFlow
import io.edugma.core.api.utils.map
import io.edugma.features.schedule.domain.model.review.LessonTimesReview
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.repository.LessonsReviewRepository
import io.edugma.features.schedule.domain.repository.ScheduleRepository

class LessonsReviewRepositoryImpl(
    private val scheduleRepository: ScheduleRepository,
) : LessonsReviewRepository {

    override fun getLessonsReview(source: ScheduleSource): LceFlow<List<LessonTimesReview>> {
        return scheduleRepository.getRawSchedule(source).map { schedule ->
            val lessonsBySubjectId = schedule.lessons.groupBy { it.subjectId }

            lessonsBySubjectId.map {
                LessonTimesReview(
                    subject = schedule.getSubject(it.key),
                    events = it.value,
                )
            }
        }
    }
}
