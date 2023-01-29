package io.edugma.data.schedule.api

import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface ScheduleSourcesService {
    @GET("/schedule/sources/{type}")
    fun getSources(
        @Path("type") type: String,
    ): Flow<Result<List<ScheduleSourceFull>>>

    @GET("/schedule/sources")
    fun getSourceTypes(): Flow<Result<List<ScheduleSources>>>
}
