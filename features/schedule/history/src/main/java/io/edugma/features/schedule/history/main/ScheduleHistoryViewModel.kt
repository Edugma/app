package io.edugma.features.schedule.history.main

import androidx.lifecycle.viewModelScope
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.usecase.ScheduleHistoryUseCase
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.navigation.schedule.ScheduleHistoryScreens
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class ScheduleHistoryViewModel(
    private val useCase: ScheduleHistoryUseCase
) : BaseViewModel<ScheduleHistoryState>(ScheduleHistoryState()) {
    init {
        viewModelScope.launch {
            useCase.getHistory().collect {
                it.onSuccess {
                    mutateState {
                        state = state.copy(history = it)
                    }
                }
            }
        }
    }

    fun onScheduleClick(dateTime: Instant) {
        router.navigateTo(
            ScheduleHistoryScreens.Changes(
                first = dateTime,
                second = dateTime
            )
        )
    }
}

data class ScheduleHistoryState(
    val history: Map<Instant, List<ScheduleDay>> = emptyMap()
)