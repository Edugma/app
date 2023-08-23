package io.edugma.features.schedule.history.presentation.changes

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.navigation.schedule.ScheduleHistoryScreens
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.schedule.domain.usecase.ScheduleDayChange
import io.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase
import io.edugma.navigation.core.screen.NavArgs
import kotlinx.datetime.Instant

class ScheduleChangesViewModel(
    private val useCase: ScheduleHistoryUseCase,
) : BaseActionViewModel<ScheduleChangesState, ScheduleChangesAction>(ScheduleChangesState()) {

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

data class ScheduleChangesState(
    val firstSelected: Instant? = null,
    val secondSelected: Instant? = null,
    val changes: List<ScheduleDayChange> = emptyList(),
)

sealed interface ScheduleChangesAction {
    data class OnArguments(
        val args: NavArgs<ScheduleHistoryScreens.Changes>,
    ) : ScheduleChangesAction
}
