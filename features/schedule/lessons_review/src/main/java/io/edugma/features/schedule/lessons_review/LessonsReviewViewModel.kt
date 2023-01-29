package io.edugma.features.schedule.lessons_review

import androidx.lifecycle.viewModelScope
import io.edugma.domain.schedule.model.review.LessonTimesReview
import io.edugma.features.schedule.domain.usecase.LessonsReviewUseCase
import io.edugma.features.base.core.mvi.BaseViewModel
import kotlinx.coroutines.launch

class LessonsReviewViewModel(
    private val useCase: LessonsReviewUseCase,
) : BaseViewModel<LessonsReviewState>(LessonsReviewState()) {

    init {
        viewModelScope.launch {
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
