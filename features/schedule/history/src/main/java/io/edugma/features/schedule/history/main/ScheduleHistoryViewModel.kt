package io.edugma.features.schedule.history.main

import io.edugma.core.navigation.schedule.ScheduleHistoryScreens
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import io.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase
import kotlinx.datetime.Instant

class ScheduleHistoryViewModel(
    private val useCase: ScheduleHistoryUseCase,
) : BaseViewModel<ScheduleHistoryState>(ScheduleHistoryState()) {
    init {
        launchCoroutine {
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
                second = dateTime,
            ),
        )
    }
}

data class ScheduleHistoryState(
    val history: Map<Instant, List<ScheduleDay>> = emptyMap(),
)
