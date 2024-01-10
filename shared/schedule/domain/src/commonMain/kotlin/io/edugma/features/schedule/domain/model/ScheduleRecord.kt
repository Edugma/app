package io.edugma.features.schedule.domain.model

import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import kotlinx.datetime.Instant

data class ScheduleRecord(
    val schedule: CompactSchedule,
    val timestamp: Instant,
)
