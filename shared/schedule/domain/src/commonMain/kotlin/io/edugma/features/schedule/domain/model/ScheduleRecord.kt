package io.edugma.features.schedule.domain.model

import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import kotlinx.datetime.Instant

data class ScheduleRecord(
    val schedule: List<ScheduleDay>,
    val timestamp: Instant,
)
