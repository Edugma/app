package io.edugma.features.schedule.domain.repository

import io.edugma.features.schedule.domain.model.group.GroupInfo
import io.edugma.features.schedule.domain.model.lesson_subject.LessonSubjectInfo
import io.edugma.features.schedule.domain.model.lesson_type.LessonTypeInfo
import io.edugma.features.schedule.domain.model.place.PlaceInfo
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import kotlinx.coroutines.flow.Flow

interface ScheduleInfoRepository {
    fun getTeacherInfo(id: String): Flow<Result<TeacherInfo>>
    fun getGroupInfo(id: String): Flow<Result<GroupInfo>>
    fun getPlaceInfo(id: String): Flow<Result<PlaceInfo>>
    fun getLessonSubjectInfo(id: String): Flow<Result<LessonSubjectInfo>>
    fun getLessonTypeInfo(id: String): Flow<Result<LessonTypeInfo>>
}
