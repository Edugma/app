package io.edugma.features.schedule.calendar

import androidx.lifecycle.viewModelScope
import io.edugma.domain.base.utils.getOrDefault
import io.edugma.domain.base.utils.isFinalFailure
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.usecase.ScheduleUseCase
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.features.base.core.mvi.BaseViewModelFull
import kotlinx.coroutines.launch

class ScheduleCalendarViewModel(
    private val useCase: ScheduleUseCase
) : BaseViewModelFull<ScheduleCalendarState, ScheduleCalendarMutator, Nothing>(
    ScheduleCalendarState(),
    ::ScheduleCalendarMutator
){
    init {
        viewModelScope.launch {
            useCase.getSchedule().collect {
                if (!it.isFinalFailure) {
                    mutateState {
                        setSchedule(it.getOrDefault(emptyList()))
                    }
                }
            }
        }
    }
}

data class ScheduleCalendarState(
    val schedule: List<ScheduleDay> = emptyList()
)

class ScheduleCalendarMutator : BaseMutator<ScheduleCalendarState>() {
    fun setSchedule(schedule: List<ScheduleDay>) {
        if (state.schedule != schedule) {
            state = state.copy(schedule = schedule)
        }
    }
}