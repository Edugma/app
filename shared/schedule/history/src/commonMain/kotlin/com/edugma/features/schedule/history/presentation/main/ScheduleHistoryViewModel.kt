package com.edugma.features.schedule.history.presentation.main

import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import com.edugma.core.navigation.schedule.ScheduleHistoryScreens
import com.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase

class ScheduleHistoryViewModel(
    private val useCase: ScheduleHistoryUseCase,
) : BaseActionViewModel<ScheduleHistoryUiState, ScheduleHistoryAction>(ScheduleHistoryUiState()) {
    init {
        launchCoroutine {
            useCase.getHistory().collect {
                newState {
                    toHistory(history = it)
                }
            }
        }
    }

    override fun processAction(action: ScheduleHistoryAction) {
        when (action) {
            ScheduleHistoryAction.OnCompareClicked -> {
                val firstSelected = state.previousSelected
                val secondSelected = state.nextSelected
                if (firstSelected != null && secondSelected != null) {
                    scheduleRouter.navigateTo(
                        ScheduleHistoryScreens.Changes(
                            first = firstSelected,
                            second = secondSelected,
                        ),
                    )
                }
            }
            is ScheduleHistoryAction.OnScheduleSelected -> {
                newState {
                    state.toSelected(action.timestamp)
                }
            }

            ScheduleHistoryAction.OnBack -> exit()
        }
    }

    fun exit() {
        scheduleRouter.back()
    }
}
