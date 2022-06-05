package io.edugma.data.schedule.api

import io.edugma.data.schedule.model.LoginRequest
import io.edugma.data.schedule.model.ScheduleComplexRequest
import io.edugma.domain.schedule.model.compact.CompactSchedule
import io.edugma.domain.schedule.model.source.ScheduleSource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ScheduleService {
    // Compact
    @GET("/schedules/compact/{type}/{key}")
    fun getCompactSchedule(
        @Path("type") type: String,
        @Path("key") key: String
    ): Flow<Result<CompactSchedule>>

    @POST("/schedules/compact/complex")
    fun getFilteredCompactSchedule(
        @Body filters: ScheduleComplexRequest
    ): Flow<Result<CompactSchedule>>


    // My
    @GET("/schedules/my")
    fun getMySchedule(): Flow<Result<CompactSchedule>>

    @POST("/login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Flow<Result<CompactSchedule>>
}

fun ScheduleService.getCompactSchedule(source: ScheduleSource) =
    getCompactSchedule(source.type.name.lowercase(), source.key)