package com.edugma.features.schedule.domain.model

import com.edugma.features.schedule.domain.model.compact.CompactSchedule
import kotlin.time.Instant

data class ScheduleRecord(
    val schedule: CompactSchedule,
    val timestamp: Instant,
)
