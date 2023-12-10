package io.edugma.features.schedule.calendar.model

import androidx.compose.runtime.Immutable
import io.edugma.features.schedule.domain.model.compact.Importance

@Immutable
data class CalendarLessonVO(
    val title: String,
    val importance: Importance,
)
