package io.edugma.data.schedule.api

import io.edugma.features.schedule.domain.model.group.GroupInfo
import io.edugma.features.schedule.domain.model.lessonSubject.LessonSubjectInfo
import io.edugma.features.schedule.domain.model.lessonType.LessonTypeInfo
import io.edugma.features.schedule.domain.model.place.PlaceInfo
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface ScheduleInfoService {
    @GET("/schedule/info/group/{key}")
    suspend fun getGroupInfo(
        @Path("key") key: String,
    ): Result<GroupInfo>

    @GET("/schedule/info/teacher/{key}")
    suspend fun getTeacherInfo(
        @Path("key") key: String,
    ): Result<TeacherInfo>

    @GET("/schedule/info/place/{key}")
    suspend fun getPlaceInfo(
        @Path("key") key: String,
    ): Result<PlaceInfo>

    @GET("/schedule/info/subject/{key}")
    suspend fun getSubjectInfo(
        @Path("key") key: String,
    ): Result<LessonSubjectInfo>

    @GET("/schedule/info/lesson-type/{key}")
    suspend fun getLessonTypeInfo(
        @Path("key") key: String,
    ): Result<LessonTypeInfo>
}
