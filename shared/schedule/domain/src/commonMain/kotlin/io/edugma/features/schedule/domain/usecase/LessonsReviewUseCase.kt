package io.edugma.features.schedule.domain.usecase

import io.edugma.core.api.utils.LceFlow
import io.edugma.features.schedule.domain.model.review.LessonTimesReview
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.repository.LessonsReviewRepository
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository

class LessonsReviewUseCase(
    private val repository: LessonsReviewRepository,
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
) {
    suspend fun getLessonsReview(): LceFlow<List<LessonTimesReview>> {
        val source = scheduleSourcesRepository.getSelectedSourceSuspend()

        return if (source == null) {
            LceFlow.empty()
        } else {
            repository.getLessonsReview(ScheduleSource(source.type, source.id))
        }
    }
}
