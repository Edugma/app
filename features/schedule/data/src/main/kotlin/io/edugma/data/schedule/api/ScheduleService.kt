package io.edugma.data.schedule.api

import io.edugma.data.schedule.model.LoginRequest
import io.edugma.data.schedule.model.ScheduleComplexRequest
import io.edugma.features.schedule.domain.model.ScheduleComplexFilter
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ScheduleService {
    // Compact
    @GET("/schedules/compact/{type}/{key}")
    suspend fun getCompactSchedule(
        @Path("type") type: String,
        @Path("key") key: String,
    ): Result<CompactSchedule>

    @POST("/schedules/compact/complex")
    suspend fun getComplexSchedule(
        @Body filter: ScheduleComplexFilter,
    ): Result<CompactSchedule>

    @POST("/schedules/compact/complex")
    suspend fun getFilteredCompactSchedule(
        @Body filters: ScheduleComplexRequest,
    ): Result<CompactSchedule>

    // My
    @GET("/schedules/my")
    suspend fun getMySchedule(): Result<CompactSchedule>

    @POST("/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Result<CompactSchedule>
}

suspend fun ScheduleService.getCompactSchedule(source: ScheduleSource) =
    getCompactSchedule(source.type.name.lowercase(), source.key)
