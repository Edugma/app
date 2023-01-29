package io.edugma.data.schedule.api

import io.edugma.domain.schedule.model.group.GroupInfo
import io.edugma.domain.schedule.model.lesson_subject.LessonSubjectInfo
import io.edugma.domain.schedule.model.lesson_type.LessonTypeInfo
import io.edugma.domain.schedule.model.place.PlaceInfo
import io.edugma.domain.schedule.model.teacher.TeacherInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface ScheduleInfoService {
    @GET("/schedule/info/group/{key}")
    fun getGroupInfo(
        @Path("key") key: String,
    ): Flow<Result<GroupInfo>>

    @GET("/schedule/info/teacher/{key}")
    fun getTeacherInfo(
        @Path("key") key: String,
    ): Flow<Result<TeacherInfo>>

    @GET("/schedule/info/place/{key}")
    fun getPlaceInfo(
        @Path("key") key: String,
    ): Flow<Result<PlaceInfo>>

    @GET("/schedule/info/subject/{key}")
    fun getSubjectInfo(
        @Path("key") key: String,
    ): Flow<Result<LessonSubjectInfo>>

    @GET("/schedule/info/lesson-type/{key}")
    fun getLessonTypeInfo(
        @Path("key") key: String,
    ): Flow<Result<LessonTypeInfo>>
}
