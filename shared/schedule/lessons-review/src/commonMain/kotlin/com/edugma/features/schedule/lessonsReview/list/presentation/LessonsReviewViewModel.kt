package com.edugma.features.schedule.lessonsReview.list.presentation

import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import com.edugma.core.utils.lce.launchLce
import com.edugma.features.schedule.lessonsReview.list.domain.LessonsReviewUseCase

class LessonsReviewViewModel(
    private val useCase: LessonsReviewUseCase,
) : FeatureLogic<LessonsReviewUiState, LessonsReviewAction>() {
    override fun initialState(): LessonsReviewUiState {
        return LessonsReviewUiState()
    }

    override fun onCreate() {
        loadLessonsReview(isRefreshing = false)
    }

    override fun processAction(action: LessonsReviewAction) {
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
                        lessons = it.valueOrThrow,
                    )
                }
            },
        )
    }

    fun exit() {
        scheduleRouter.back()
    }
}
