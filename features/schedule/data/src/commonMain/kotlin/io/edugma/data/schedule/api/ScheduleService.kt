package io.edugma.data.schedule.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import io.edugma.data.schedule.model.LoginRequest
import io.edugma.data.schedule.model.ScheduleComplexRequest
import io.edugma.features.schedule.domain.model.ScheduleComplexFilter
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.source.ScheduleSource

interface ScheduleService {
    // Compact
    @GET("schedules/compact/{type}/{key}")
    suspend fun getCompactSchedule(
        @Path("type") type: String,
        @Path("key") key: String,
    ): Result<CompactSchedule>

    @Headers("Content-Type: application/json")
    @POST("schedules/compact/complex")
    suspend fun getComplexSchedule(
        @Body filter: ScheduleComplexFilter,
    ): Result<CompactSchedule>

    @Headers("Content-Type: application/json")
    @POST("schedules/compact/complex")
    suspend fun getFilteredCompactSchedule(
        @Body filters: ScheduleComplexRequest,
    ): Result<CompactSchedule>

    // My
    @GET("schedules/my")
    suspend fun getMySchedule(): Result<CompactSchedule>

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Result<CompactSchedule>
}

suspend fun ScheduleService.getCompactSchedule(source: ScheduleSource) =
    getCompactSchedule(source.type.name.lowercase(), source.key)
