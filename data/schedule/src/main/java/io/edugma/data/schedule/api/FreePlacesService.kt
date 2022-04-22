package io.edugma.data.schedule.api

import io.edugma.domain.schedule.model.place.PlaceDailyOccupancy
import io.edugma.domain.schedule.model.place.PlaceFilters
import io.edugma.domain.schedule.model.place.PlaceInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface FreePlacesService {
    @POST("/schedule/free-place")
    fun findFreePlaces(
        @Body filters: PlaceFilters
    ): Flow<Result<Map<PlaceInfo, Int>>>

    @POST("/schedule/places/occupancy/{placeId}")
    fun getPlaceOccupancy(
        @Path("placeId") placeId: String,
    ): Flow<Result<List<PlaceDailyOccupancy>>>
}