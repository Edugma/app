package io.edugma.data.schedule.api

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSources

interface ScheduleSourcesService {
    @GET("schedule/sources/{type}")
    suspend fun getSources(
        @Path("type") type: String,
    ): Result<List<ScheduleSourceFull>>

    @GET("schedule/sources")
    suspend fun getSourceTypes(): Result<List<ScheduleSources>>
}
