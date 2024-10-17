package com.edugma.data.schedule.repository

import com.edugma.data.schedule.api.ScheduleService
import com.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import com.edugma.features.schedule.domain.model.place.PlaceDailyOccupancy
import com.edugma.features.schedule.domain.model.place.PlaceFilters
import com.edugma.features.schedule.domain.repository.FreePlaceRepository

class FreePlaceRepositoryImpl(
    private val scheduleService: ScheduleService,
) : FreePlaceRepository {

    override suspend fun findFreePlaces(filters: PlaceFilters): Map<CompactPlaceInfo, Int> {
        return scheduleService.findFreePlaces(filters)
    }

    override suspend fun getPlaceOccupancy(placeId: String): List<PlaceDailyOccupancy> {
        return scheduleService.getPlaceOccupancy(placeId)
    }
}
