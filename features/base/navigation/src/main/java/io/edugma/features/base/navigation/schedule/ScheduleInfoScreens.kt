package io.edugma.features.base.navigation.schedule

import io.edugma.features.base.core.navigation.core.Screen

object ScheduleInfoScreens {
    class LessonInfo(
        val lessonInfo: io.edugma.domain.schedule.model.lesson.LessonInfo,
    ) : Screen(
        LessonInfo::lessonInfo.name to lessonInfo.serialized(),
    )

    class SubjectInfo(
        val id: String,
    ) : Screen(
        SubjectInfo::id.name to id,
    )

    class TeacherInfo(
        val id: String,
    ) : Screen(
        TeacherInfo::id.name to id,
    )

    class GroupInfo(
        val id: String,
    ) : Screen(
        GroupInfo::id.name to id,
    )

    class PlaceInfo(
        val id: String,
    ) : Screen(
        PlaceInfo::id.name to id,
    )
}
