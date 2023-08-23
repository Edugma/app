package io.edugma.features.schedule.history.presentation.main

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.navigation.schedule.ScheduleHistoryScreens
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.schedule.domain.model.ScheduleRecord
import io.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase
import kotlinx.datetime.Instant

class ScheduleHistoryViewModel(
    private val useCase: ScheduleHistoryUseCase,
) : BaseActionViewModel<ScheduleHistoryState, ScheduleHistoryAction>(ScheduleHistoryState()) {
    init {
        launchCoroutine {
            useCase.getHistory().collect {
                newState {
                    onHistory(history = it)
                }
            }
        }
    }

    override fun onAction(action: ScheduleHistoryAction) {
        when (action) {
            ScheduleHistoryAction.OnCompareClicked -> {
                val firstSelected = state.firstSelected
                val secondSelected = state.secondSelected
                if (firstSelected != null && secondSelected != null) {
                    router.navigateTo(
                        ScheduleHistoryScreens.Changes(
                            first = firstSelected,
                            second = secondSelected,
                        ),
                    )
                }
            }
            is ScheduleHistoryAction.OnScheduleSelected -> {
                newState {
                    state.toSelected(action.timestamp)
                }
            }

            ScheduleHistoryAction.OnBack -> router.back()
        }
    }
}

data class ScheduleHistoryState(
    val history: List<ScheduleRecord> = emptyList(),
    val firstSelected: Instant? = null,
    val secondSelected: Instant? = null,
    val isCheckButtonEnabled: Boolean = false,
) {
    fun onHistory(history: List<ScheduleRecord>): ScheduleHistoryState {
        return if (firstSelected != null) {
            if (secondSelected != null) {
                copy(
                    history = history,
                )
            } else {
                copy(
                    history = history,
                    secondSelected = history
                        .firstOrNull { it.timestamp != firstSelected }?.timestamp,
                )
            }
        } else {
            copy(
                history = history,
                firstSelected = history.getOrNull(0)?.timestamp,
                secondSelected = history.getOrNull(1)?.timestamp,
            )
        }.updateIsCheckButtonEnabled()
    }

    fun toSelected(timestamp: Instant): ScheduleHistoryState {
        return when {
            timestamp == firstSelected -> copy(
                firstSelected = secondSelected,
                secondSelected = null,
            )

            timestamp == secondSelected -> copy(
                secondSelected = null,
            )

            firstSelected != null -> if (secondSelected != null) {
                copy(
                    secondSelected = firstSelected,
                    firstSelected = timestamp,
                )
            } else {
                copy(
                    secondSelected = timestamp,
                )
            }

            else -> copy(
                firstSelected = timestamp,
            )
        }.updateIsCheckButtonEnabled()
    }

    private fun updateIsCheckButtonEnabled(): ScheduleHistoryState {
        return if (firstSelected != null && secondSelected != null) {
            copy(isCheckButtonEnabled = true)
        } else {
            copy(isCheckButtonEnabled = false)
        }
    }
}

sealed interface ScheduleHistoryAction {
    data class OnScheduleSelected(val timestamp: Instant) : ScheduleHistoryAction
    object OnCompareClicked : ScheduleHistoryAction
    object OnBack : ScheduleHistoryAction
}
