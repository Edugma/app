package io.edugma.features.schedule.domain.usecase

import io.edugma.domain.schedule.model.review.LessonTimesReview
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.features.schedule.domain.repository.LessonsReviewRepository
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest

class LessonsReviewUseCase(
    private val repository: LessonsReviewRepository,
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
) {
    fun getLessonsReview() =
        scheduleSourcesRepository.getSelectedSource()
            .transformLatest {
                val source = it.getOrNull()
                if (source == null) {
                    emit(Result.failure<List<LessonTimesReview>>(Exception()))
                } else {
                    emitAll(repository.getLessonsReview(ScheduleSource(source.type, source.key)))
                }
            }
}
