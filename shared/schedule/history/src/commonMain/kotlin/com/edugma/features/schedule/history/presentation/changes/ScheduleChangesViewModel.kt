package com.edugma.features.schedule.history.presentation.changes

import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import com.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase
import kotlinx.datetime.Instant

class ScheduleChangesViewModel(
    private val useCase: ScheduleHistoryUseCase,
) : BaseActionViewModel<ScheduleChangesUiState, ScheduleChangesAction>(ScheduleChangesUiState()) {

    override fun processAction(action: ScheduleChangesAction) {
        when (action) {
            is ScheduleChangesAction.OnArguments -> {
                newState {
                    copy(
                        firstSelected = Instant.fromEpochMilliseconds(action.args { destination.first.get() }),
                        secondSelected = Instant.fromEpochMilliseconds(action.args { destination.second.get() }),
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

    fun exit() {
        scheduleRouter.back()
    }
}
