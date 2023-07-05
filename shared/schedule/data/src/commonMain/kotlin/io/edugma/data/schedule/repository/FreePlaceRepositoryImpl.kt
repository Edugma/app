package io.edugma.data.schedule.repository

import io.edugma.data.schedule.api.FreePlacesService
import io.edugma.features.schedule.domain.model.place.PlaceFilters
import io.edugma.features.schedule.domain.repository.FreePlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FreePlaceRepositoryImpl(
    private val freePlacesService: FreePlacesService,
) : FreePlaceRepository {

    override fun findFreePlaces(filters: PlaceFilters) =
        flow { emit(freePlacesService.findFreePlaces(filters)) }
            .flowOn(Dispatchers.IO)

    override fun getPlaceOccupancy(placeId: String) =
        flow { emit(freePlacesService.getPlaceOccupancy(placeId)) }
            .flowOn(Dispatchers.IO)
}
