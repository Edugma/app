package io.edugma.data.schedule.api

import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import retrofit2.http.GET
import retrofit2.http.Path

interface ScheduleSourcesService {
    @GET("/schedule/sources/{type}")
    suspend fun getSources(
        @Path("type") type: String,
    ): Result<List<ScheduleSourceFull>>

    @GET("/schedule/sources")
    suspend fun getSourceTypes(): Result<List<ScheduleSources>>
}
