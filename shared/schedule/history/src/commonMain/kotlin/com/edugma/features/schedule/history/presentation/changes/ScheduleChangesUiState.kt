package com.edugma.features.schedule.history.presentation.changes

import androidx.compose.runtime.Immutable
import com.edugma.features.schedule.domain.usecase.LessonChange
import kotlinx.datetime.Instant

@Immutable
data class ScheduleChangesUiState(
    val firstSelected: Instant? = null,
    val secondSelected: Instant? = null,
    val changes: List<LessonChange> = emptyList(),
)
