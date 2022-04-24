package io.edugma.domain.schedule.repository

import io.edugma.domain.schedule.model.place.PlaceDailyOccupancy
import io.edugma.domain.schedule.model.place.PlaceFilters
import io.edugma.domain.schedule.model.place.PlaceInfo
import kotlinx.coroutines.flow.Flow

interface FreePlaceRepository {
    fun findFreePlaces(filters: PlaceFilters): Flow<Result<Map<PlaceInfo, Int>>>
    fun getPlaceOccupancy(placeId: String): Flow<Result<List<PlaceDailyOccupancy>>>
}