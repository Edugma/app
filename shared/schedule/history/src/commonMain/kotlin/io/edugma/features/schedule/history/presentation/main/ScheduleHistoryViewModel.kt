package io.edugma.features.schedule.history.presentation.main

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.navigation.schedule.ScheduleHistoryScreens
import io.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase

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

    override fun onAction(action: ScheduleHistoryAction) {
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
