package io.edugma.features.schedule.domain.repository

import io.edugma.features.schedule.domain.model.place.PlaceDailyOccupancy
import io.edugma.features.schedule.domain.model.place.PlaceFilters
import io.edugma.features.schedule.domain.model.place.PlaceInfo
import kotlinx.coroutines.flow.Flow

interface FreePlaceRepository {
    fun findFreePlaces(filters: PlaceFilters): Flow<Result<Map<PlaceInfo, Int>>>
    fun getPlaceOccupancy(placeId: String): Flow<Result<List<PlaceDailyOccupancy>>>
}
