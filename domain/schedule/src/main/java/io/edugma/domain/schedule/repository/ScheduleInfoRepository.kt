package io.edugma.domain.schedule.repository

import io.edugma.domain.schedule.model.group.GroupInfo
import io.edugma.domain.schedule.model.lesson_subject.LessonSubjectInfo
import io.edugma.domain.schedule.model.lesson_type.LessonTypeInfo
import io.edugma.domain.schedule.model.place.PlaceInfo
import io.edugma.domain.schedule.model.teacher.TeacherInfo
import kotlinx.coroutines.flow.Flow

interface ScheduleInfoRepository {
    fun getTeacherInfo(id: String): Flow<Result<TeacherInfo>>
    fun getGroupInfo(id: String): Flow<Result<GroupInfo>>
    fun getPlaceInfo(id: String): Flow<Result<PlaceInfo>>
    fun getLessonSubjectInfo(id: String): Flow<Result<LessonSubjectInfo>>
    fun getLessonTypeInfo(id: String): Flow<Result<LessonTypeInfo>>
}