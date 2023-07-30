package io.edugma.features.schedule.history.changes

import io.edugma.core.arch.mvi.updateState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.schedule.domain.usecase.ScheduleDayChange
import io.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase

class ScheduleChangesViewModel(
    private val useCase: ScheduleHistoryUseCase,
) : BaseViewModel<ScheduleChangesState>(ScheduleChangesState()) {

    init {
        launchCoroutine {
            val changes = useCase.getChanges2()
            updateState {
                copy(changes = changes)
            }
        }
    }
}

data class ScheduleChangesState(
    val changes: List<ScheduleDayChange> = emptyList(),
)
