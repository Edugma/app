package io.edugma.data.schedule.api

import io.edugma.data.schedule.model.LoginRequest
import io.edugma.data.schedule.model.ScheduleComplexRequest
import io.edugma.domain.schedule.model.compact.CompactSchedule
import io.edugma.domain.schedule.model.group.GroupInfo
import io.edugma.domain.schedule.model.lesson.LessonDateTimes
import io.edugma.domain.schedule.model.lesson_subject.LessonSubjectInfo
import io.edugma.domain.schedule.model.lesson_type.LessonTypeInfo
import io.edugma.domain.schedule.model.place.Place
import io.edugma.domain.schedule.model.place.PlaceDailyOccupancy
import io.edugma.domain.schedule.model.place.PlaceFilters
import io.edugma.domain.schedule.model.place.PlaceInfo
import io.edugma.domain.schedule.model.review.LessonTimesReview
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import io.edugma.domain.schedule.model.source.ScheduleSources
import io.edugma.domain.schedule.model.teacher.TeacherInfo
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