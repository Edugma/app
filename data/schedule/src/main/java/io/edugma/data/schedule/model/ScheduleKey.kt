package io.edugma.data.schedule.model

import io.edugma.domain.schedule.model.source.ScheduleSource
import kotlinx.datetime.Instant

data class ScheduleKey(
    val source: ScheduleSource,
    val date: Instant
)