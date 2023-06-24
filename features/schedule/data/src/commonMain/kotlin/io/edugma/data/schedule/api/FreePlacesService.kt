package io.edugma.data.schedule.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import io.edugma.features.schedule.domain.model.place.PlaceDailyOccupancy
import io.edugma.features.schedule.domain.model.place.PlaceFilters
import io.edugma.features.schedule.domain.model.place.PlaceInfo

interface FreePlacesService {
    @Headers("Content-Type: application/json")
    @POST("schedule/places/free")
    suspend fun findFreePlaces(
        @Body filters: PlaceFilters,
    ): Result<Map<PlaceInfo, Int>>

    @GET("schedule/places/occupancy/{placeId}")
    suspend fun getPlaceOccupancy(
        @Path("placeId") placeId: String,
    ): Result<List<PlaceDailyOccupancy>>
}
