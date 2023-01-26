package io.edugma.domain.schedule.model.compact

import io.edugma.domain.schedule.model.group.GroupInfo
import io.edugma.domain.schedule.model.lesson_subject.LessonSubjectInfo
import io.edugma.domain.schedule.model.lesson_type.LessonTypeInfo
import io.edugma.domain.schedule.model.place.PlaceInfo
import io.edugma.domain.schedule.model.teacher.TeacherInfo
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleInfo(
    val typesInfo: List<LessonTypeInfo>,
    val subjectsInfo: List<LessonSubjectInfo>,
    val teachersInfo: List<TeacherInfo>,
    val groupsInfo: List<GroupInfo>,
    val placesInfo: List<PlaceInfo>,
)
