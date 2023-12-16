package io.edugma.features.schedule.lessonsReview

import io.edugma.core.api.utils.onResult
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.features.schedule.domain.model.review.LessonTimesReview
import io.edugma.features.schedule.domain.usecase.LessonsReviewUseCase

class LessonsReviewViewModel(
    private val useCase: LessonsReviewUseCase,
) : BaseViewModel<LessonsReviewState>(LessonsReviewState()) {

    init {
        launchCoroutine {
            useCase.getLessonsReview().onResult(
                onSuccess = {
                    newState {
                        copy(
                            lessons = it.value,
                        )
                    }
                },
                onFailure = {},
            )
        }
    }
}

data class LessonsReviewState(
    val lessons: List<LessonTimesReview> = emptyList(),
)
