package io.edugma.features.base.navigation.schedule

import io.edugma.features.base.core.navigation.core.Screen

object ScheduleInfoScreens {
    class TeacherInfo(
        val id: String
    ) : Screen(
        PlaceInfo::id.name to id
    )

    class GroupInfo(
        val id: String
    ) : Screen(
        PlaceInfo::id.name to id
    )

    class PlaceInfo(
        val id: String
    ) : Screen(
        PlaceInfo::id.name to id
    )
}