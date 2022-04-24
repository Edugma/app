package io.edugma.features.schedule.calendar

import androidx.lifecycle.viewModelScope
import io.edugma.domain.base.utils.getOrDefault
import io.edugma.domain.base.utils.isFinalFailure
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.usecase.ScheduleUseCase
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.BaseViewModelFull
import io.edugma.features.schedule.calendar.model.ScheduleCalendarWeek
import io.edugma.features.schedule.calendar.model.toCalendarUiModel
import kotlinx.coroutines.launch

class ScheduleCalendarViewModel(
    private val useCase: ScheduleUseCase
) : BaseViewModel<ScheduleCalendarState>(ScheduleCalendarState()){
    init {
        viewModelScope.launch {
            useCase.getSchedule().collect {
                if (!it.isFinalFailure) {
                    mutateState {

                        val schedule = it.getOrDefault(emptyList()).toCalendarUiModel()

                        if (state.schedule != schedule) {
                            state = state.copy(schedule = schedule)
                        }
                    }
                }
            }
        }
    }
}

data class ScheduleCalendarState(
    val schedule: List<ScheduleCalendarWeek> = emptyList()
)