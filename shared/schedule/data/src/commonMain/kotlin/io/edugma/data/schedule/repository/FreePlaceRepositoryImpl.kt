package io.edugma.data.schedule.repository

import io.edugma.core.api.utils.IO
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.features.schedule.domain.model.place.PlaceFilters
import io.edugma.features.schedule.domain.repository.FreePlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FreePlaceRepositoryImpl(
    private val scheduleService: ScheduleService,
) : FreePlaceRepository {

    override fun findFreePlaces(filters: PlaceFilters) =
        flow { emit(scheduleService.findFreePlaces(filters)) }
            .flowOn(Dispatchers.IO)

    override fun getPlaceOccupancy(placeId: String) =
        flow { emit(scheduleService.getPlaceOccupancy(placeId)) }
            .flowOn(Dispatchers.IO)
}
