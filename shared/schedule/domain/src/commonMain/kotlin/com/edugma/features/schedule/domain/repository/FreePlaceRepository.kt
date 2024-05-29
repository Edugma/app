package com.edugma.features.schedule.domain.repository

import com.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import com.edugma.features.schedule.domain.model.place.PlaceDailyOccupancy
import com.edugma.features.schedule.domain.model.place.PlaceFilters
import kotlinx.coroutines.flow.Flow

interface FreePlaceRepository {
    fun findFreePlaces(filters: PlaceFilters): Flow<Result<Map<CompactPlaceInfo, Int>>>
    fun getPlaceOccupancy(placeId: String): Flow<Result<List<PlaceDailyOccupancy>>>
}
