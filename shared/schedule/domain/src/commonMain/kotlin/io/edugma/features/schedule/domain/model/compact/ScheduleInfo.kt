package io.edugma.features.schedule.domain.model.compact

import io.edugma.features.schedule.domain.model.group.GroupInfo
import io.edugma.features.schedule.domain.model.lessonSubject.LessonSubjectInfo
import io.edugma.features.schedule.domain.model.lessonType.LessonTypeInfo
import io.edugma.features.schedule.domain.model.place.PlaceInfo
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleInfo(
    val typesInfo: List<LessonTypeInfo>,
    val subjectsInfo: List<LessonSubjectInfo>,
    val teachersInfo: List<TeacherInfo>,
    val groupsInfo: List<GroupInfo>,
    val placesInfo: List<PlaceInfo>,
)
