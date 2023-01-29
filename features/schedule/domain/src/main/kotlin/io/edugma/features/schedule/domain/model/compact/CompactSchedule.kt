package io.edugma.features.schedule.domain.model.compact

import kotlinx.serialization.Serializable

@Serializable
data class CompactSchedule(
    val lessons: List<CompactLessonAndTimes>,
    val info: ScheduleInfo,
)
