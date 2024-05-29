package com.edugma.features.schedule.domain.model.place

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDailyOccupancy(
    val date: LocalDate,
    val values: List<PlaceOccupancyTimeRange>,
)
