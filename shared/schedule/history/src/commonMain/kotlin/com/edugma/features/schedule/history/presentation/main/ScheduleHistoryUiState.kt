package com.edugma.features.schedule.history.presentation.main

import androidx.compose.runtime.Immutable
import com.edugma.features.schedule.domain.model.ScheduleRecord
import kotlinx.datetime.Instant

@Immutable
data class ScheduleHistoryUiState(
    val history: List<ScheduleRecord> = emptyList(),
    val previousSelected: Instant? = null,
    val nextSelected: Instant? = null,
    val isCheckButtonEnabled: Boolean = false,
) {
    fun toHistory(history: List<ScheduleRecord>): ScheduleHistoryUiState {
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

    fun toSelected(timestamp: Instant): ScheduleHistoryUiState {
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

    private fun updateIsCheckButtonEnabled(): ScheduleHistoryUiState {
        return if (previousSelected != null && nextSelected != null) {
            copy(isCheckButtonEnabled = true)
        } else {
            copy(isCheckButtonEnabled = false)
        }
    }
}
