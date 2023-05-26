package io.edugma.features.schedule.lessonsReview

import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.schedule.domain.model.review.LessonTimesReview
import io.edugma.features.schedule.domain.usecase.LessonsReviewUseCase

class LessonsReviewViewModel(
    private val useCase: LessonsReviewUseCase,
) : BaseViewModel<LessonsReviewState>(LessonsReviewState()) {

    init {
        launchCoroutine {
            useCase.getLessonsReview().collect {
                mutateState {
                    state = state.copy(
                        lessons = it.getOrDefault(emptyList()),
                    )
                }
            }
        }
    }
}

data class LessonsReviewState(
    val lessons: List<LessonTimesReview> = emptyList(),
)
