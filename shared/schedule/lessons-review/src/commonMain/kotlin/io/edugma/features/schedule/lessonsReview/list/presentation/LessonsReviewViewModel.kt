package io.edugma.features.schedule.lessonsReview.list.presentation

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.utils.lce.launchLce
import io.edugma.features.schedule.lessonsReview.list.domain.LessonsReviewUseCase

class LessonsReviewViewModel(
    private val useCase: LessonsReviewUseCase,
) : BaseActionViewModel<LessonsReviewUiState, LessonsReviewAction>(LessonsReviewUiState()) {

    init {
        loadLessonsReview(isRefreshing = false)
    }

    override fun onAction(action: LessonsReviewAction) {
        when (action) {
            LessonsReviewAction.OnRefresh -> loadLessonsReview(isRefreshing = true)
        }
    }

    private fun loadLessonsReview(isRefreshing: Boolean) {
        launchLce(
            lceProvider = {
                useCase.getLessonsReview()
            },
            getLceState = state::lceState,
            setLceState = { newState { copy(lceState = it) } },
            isContentEmpty = { state.lessons.isEmpty() },
            isRefreshing = isRefreshing,
            onSuccess = {
                newState {
                    copy(
                        lessons = it.value,
                    )
                }
            },
        )
    }
}
