package io.edugma.data.schedule.api

import io.edugma.features.schedule.domain.model.place.PlaceDailyOccupancy
import io.edugma.features.schedule.domain.model.place.PlaceFilters
import io.edugma.features.schedule.domain.model.place.PlaceInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FreePlacesService {
    @POST("/schedule/places/free")
    suspend fun findFreePlaces(
        @Body filters: PlaceFilters,
    ): Result<Map<PlaceInfo, Int>>

    @GET("/schedule/places/occupancy/{placeId}")
    suspend fun getPlaceOccupancy(
        @Path("placeId") placeId: String,
    ): Result<List<PlaceDailyOccupancy>>
}
