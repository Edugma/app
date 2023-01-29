package io.edugma.features.schedule.domain.model.place

import io.edugma.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class PlaceDailyOccupancy(
    @Serializable(with = LocalDateConverter::class)
    val date: LocalDate,
    val values: List<PlaceOccupancyTimeRange>,
)
