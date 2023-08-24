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
                    toHistory(history = it)
                }
            }
        }
    }

    override fun onAction(action: ScheduleHistoryAction) {
        when (action) {
            ScheduleHistoryAction.OnCompareClicked -> {
                val firstSelected = state.previousSelected
                val secondSelected = state.nextSelected
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
    val previousSelected: Instant? = null,
    val nextSelected: Instant? = null,
    val isCheckButtonEnabled: Boolean = false,
) {
    fun toHistory(history: List<ScheduleRecord>): ScheduleHistoryState {
        val firstIsExist = history.find { it.timestamp == previousSelected }
        return if (firstIsExist != null) {
            val secondIsExist = history.find { it.timestamp == nextSelected }
            if (secondIsExist != null) {
                copy(
                    history = history,
                )
            } else {
                copy(
                    history = history,
                    nextSelected = history
                        .firstOrNull { it.timestamp != previousSelected }?.timestamp,
                )
            }
        } else {
            copy(
                history = history,
                previousSelected = history.getOrNull(1)?.timestamp,
                nextSelected = history.getOrNull(0)?.timestamp,
            )
        }.updateIsCheckButtonEnabled()
    }

    fun toSelected(timestamp: Instant): ScheduleHistoryState {
        return when {
            timestamp == previousSelected -> copy(
                previousSelected = nextSelected,
                nextSelected = null,
            )

            timestamp == nextSelected -> copy(
                nextSelected = null,
            )

            previousSelected != null -> if (nextSelected != null) {
                copy(
                    nextSelected = previousSelected,
                    previousSelected = timestamp,
                )
            } else {
                copy(
                    nextSelected = timestamp,
                )
            }

            else -> copy(
                previousSelected = timestamp,
            )
        }.updateIsCheckButtonEnabled()
    }

    private fun updateIsCheckButtonEnabled(): ScheduleHistoryState {
        return if (previousSelected != null && nextSelected != null) {
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
