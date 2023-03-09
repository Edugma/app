package io.edugma.features.schedule.daily.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class DayUiModel(
    val date: LocalDate,
    val isToday: Boolean,
    val lessonCount: Int,
)
