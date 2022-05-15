package io.edugma.domain.schedule.model.compact

import kotlinx.serialization.Serializable

@Serializable
data class CompactSchedule(
    val lessons: List<CompactLessonAndTimes>,
    val info: ScheduleInfo
)
