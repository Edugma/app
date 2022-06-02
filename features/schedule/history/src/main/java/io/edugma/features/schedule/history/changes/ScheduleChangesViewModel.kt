package io.edugma.features.schedule.history.changes

import androidx.lifecycle.viewModelScope
import io.edugma.domain.schedule.usecase.ScheduleDayChange
import io.edugma.domain.schedule.usecase.ScheduleHistoryUseCase
import io.edugma.features.base.core.mvi.BaseViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class ScheduleChangesViewModel(
    private val useCase: ScheduleHistoryUseCase
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
    val changes: List<ScheduleDayChange> = emptyList()
)