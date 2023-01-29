package io.edugma.data.schedule.model

import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata

@Serializable
data class ScheduleDao(
    override val id: String,
    val date: Instant,
    val days: CompactSchedule?,
) : Metadata {
    companion object {
        fun from(scheduleSource: ScheduleSource, schedule: CompactSchedule?, date: Instant? = null) =
            ScheduleDao(
                id = scheduleSource.id,
                date = date ?: Clock.System.now(),
                days = schedule,
            )
    }

    fun toModel() = days
}
