package io.edugma.features.schedule.history.presentation.changes

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase

class ScheduleChangesViewModel(
    private val useCase: ScheduleHistoryUseCase,
) : BaseActionViewModel<ScheduleChangesUiState, ScheduleChangesAction>(ScheduleChangesUiState()) {

    override fun onAction(action: ScheduleChangesAction) {
        when (action) {
            is ScheduleChangesAction.OnArguments -> {
                newState {
                    copy(
                        firstSelected = action.args { screen.first.get() },
                        secondSelected = action.args { screen.second.get() },
                    )
                }
                launchCoroutine {
                    val changes = useCase.getChanges(
                        firstScheduleTimestamp = state.firstSelected!!,
                        secondScheduleTimestamp = state.secondSelected!!,
                    )
                    newState {
                        copy(
                            changes = changes,
                        )
                    }
                }
            }
        }
    }
}
