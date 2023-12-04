package io.edugma.features.schedule.domain.usecase

import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.repository.LessonsReviewRepository
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
import kotlinx.coroutines.flow.transformLatest

class LessonsReviewUseCase(
    private val repository: LessonsReviewRepository,
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
) {
    fun getLessonsReview() =
        scheduleSourcesRepository.getSelectedSource()
            .transformLatest { source ->
                checkNotNull(source)
                emit(repository.getLessonsReview(ScheduleSource(source.type, source.id)))
            }
}
