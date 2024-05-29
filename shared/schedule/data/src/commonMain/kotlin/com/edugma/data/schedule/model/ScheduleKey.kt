package com.edugma.data.schedule.model

import com.edugma.features.schedule.domain.model.source.ScheduleSource
import kotlinx.datetime.Instant

data class ScheduleKey(
    val source: ScheduleSource,
    val date: Instant,
)
