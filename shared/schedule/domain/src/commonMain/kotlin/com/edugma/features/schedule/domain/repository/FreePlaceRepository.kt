package com.edugma.features.schedule.domain.repository

import com.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import com.edugma.features.schedule.domain.model.place.PlaceDailyOccupancy
import com.edugma.features.schedule.domain.model.place.PlaceFilters

interface FreePlaceRepository {
    suspend fun findFreePlaces(filters: PlaceFilters): Map<CompactPlaceInfo, Int>
    suspend fun getPlaceOccupancy(placeId: String): List<PlaceDailyOccupancy>
}
