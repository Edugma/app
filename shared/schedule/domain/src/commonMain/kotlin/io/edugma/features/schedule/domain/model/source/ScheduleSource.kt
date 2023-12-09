package io.edugma.features.schedule.domain.model.source

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleSource(
    // val id: String,
    val type: String,
    val key: String,
) {
    val id
        get() = type + key
}
