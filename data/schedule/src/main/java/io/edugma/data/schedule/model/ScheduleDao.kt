package io.edugma.data.schedule.model

import io.edugma.domain.schedule.model.compact.CompactSchedule
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSource
import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata

@Serializable
data class ScheduleDao(
    override val id: String,
    val days: CompactSchedule?
): Metadata {
    companion object {
        fun from(scheduleSource: ScheduleSource, schedule: CompactSchedule?) =
            ScheduleDao(scheduleSource.id, schedule)
    }

    fun toModel() = days
}