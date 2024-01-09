package io.edugma.features.schedule.history.presentation.changes

import androidx.compose.runtime.Immutable
import io.edugma.features.schedule.domain.usecase.ScheduleDayChange
import kotlinx.datetime.Instant

@Immutable
data class ScheduleChangesUiState(
    val firstSelected: Instant? = null,
    val secondSelected: Instant? = null,
    val changes: List<ScheduleDayChange> = emptyList(),
)
