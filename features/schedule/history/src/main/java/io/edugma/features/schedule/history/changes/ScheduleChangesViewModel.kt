package io.edugma.features.schedule.history.changes

import androidx.lifecycle.viewModelScope
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.schedule.domain.usecase.ScheduleDayChange
import io.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase
import kotlinx.coroutines.launch

class ScheduleChangesViewModel(
    private val useCase: ScheduleHistoryUseCase,
) : BaseViewModel<ScheduleChangesState>(ScheduleChangesState()) {

    init {
        viewModelScope.launch {
            val changes = useCase.getChanges2()
            mutateState {
                state = state.copy(changes = changes)
            }
        }
    }
}

data class ScheduleChangesState(
    val changes: List<ScheduleDayChange> = emptyList(),
)
