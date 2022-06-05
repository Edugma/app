package io.edugma.data.schedule.repository

import io.edugma.data.schedule.api.FreePlacesService
import io.edugma.domain.schedule.model.place.PlaceFilters
import io.edugma.domain.schedule.repository.FreePlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class FreePlaceRepositoryImpl(
    private val freePlacesService: FreePlacesService,
) : FreePlaceRepository {

    override fun findFreePlaces(filters: PlaceFilters) =
        freePlacesService.findFreePlaces(filters)
            .flowOn(Dispatchers.IO)

    override fun getPlaceOccupancy(placeId: String) =
        freePlacesService.getPlaceOccupancy(placeId)
            .flowOn(Dispatchers.IO)
}
