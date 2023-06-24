package io.edugma.features.schedule.domain.model.place

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PlaceFilters(
    val ids: List<String>? = null,
    val dateTimeFrom: LocalDateTime,
    val dateTimeTo: LocalDateTime,
)
