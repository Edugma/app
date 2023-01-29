package io.edugma.data.schedule.model

import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata

@Serializable
data class ScheduleSourceFullDao(
    override val id: String,
    val source: ScheduleSourceFull,
) : Metadata {
    companion object {
        fun from(source: ScheduleSourceFull, id: String) =
            ScheduleSourceFullDao(
                id,
                source,
            )
    }

    fun toModel() = source
}
