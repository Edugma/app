package io.edugma.features.schedule.lessons_review

import androidx.lifecycle.viewModelScope
import io.edugma.domain.schedule.model.review.LessonTimesReview
import io.edugma.domain.schedule.usecase.ScheduleUseCase
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.features.base.core.mvi.BaseViewModelFull
import kotlinx.coroutines.launch

class LessonsReviewViewModel(
    private val useCase: ScheduleUseCase
) : BaseViewModelFull<LessonsReviewState, LessonsReviewMutator, Nothing>(
    LessonsReviewState(),
    ::LessonsReviewMutator
) {

    init {
        viewModelScope.launch {
            useCase.getLessonsReview().collect {
                mutateState {
                    setLessons(it.getOrDefault(emptyList()))
                }
            }
        }
    }
}

data class LessonsReviewState(
    val lessons: List<LessonTimesReview> = emptyList()
)

class LessonsReviewMutator : BaseMutator<LessonsReviewState>() {
    fun setLessons(lessons: List<LessonTimesReview>) {
        if (state.lessons != lessons) {
            state = state.copy(lessons = lessons)
        }
    }
}