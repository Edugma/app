package com.edugma.features.schedule.history.presentation.changes

import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import com.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase
import kotlinx.datetime.Instant

class ScheduleChangesViewModel(
    private val useCase: ScheduleHistoryUseCase,
) : FeatureLogic<ScheduleChangesUiState, ScheduleChangesAction>() {
    override fun initialState(): ScheduleChangesUiState {
        return ScheduleChangesUiState()
    }

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
