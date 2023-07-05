package io.edugma.features.schedule.calendar.model

import androidx.compose.runtime.Immutable

@Immutable
data class CalendarLessonVO(
    val title: String,
    val isImportant: Boolean,
)
