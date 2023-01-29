package io.edugma.data.schedule.model

import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata

@Serializable
data class ScheduleSourcesDao(
    override val id: String,
    val days: CompactSchedule?,
) : Metadata {
    companion object {
        fun from(scheduleSource: ScheduleSource, schedule: CompactSchedule?) =
            ScheduleSourcesDao(scheduleSource.id, schedule)
    }

    fun toModel() = days
}
