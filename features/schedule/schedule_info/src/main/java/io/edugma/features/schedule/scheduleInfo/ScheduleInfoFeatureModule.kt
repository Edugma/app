package io.edugma.features.schedule.scheduleInfo

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.schedule.ScheduleInfoScreens
import io.edugma.features.schedule.scheduleInfo.groupInfo.GroupInfoScreen
import io.edugma.features.schedule.scheduleInfo.groupInfo.GroupInfoViewModel
import io.edugma.features.schedule.scheduleInfo.lessonInfo.LessonInfoScreen
import io.edugma.features.schedule.scheduleInfo.lessonInfo.LessonInfoViewModel
import io.edugma.features.schedule.scheduleInfo.placeInfo.PlaceInfoScreen
import io.edugma.features.schedule.scheduleInfo.placeInfo.PlaceInfoViewModel
import io.edugma.features.schedule.scheduleInfo.teacherInfo.TeacherInfoScreen
import io.edugma.features.schedule.scheduleInfo.teacherInfo.TeacherInfoViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object ScheduleInfoFeatureModule {
    val deps = module {
        viewModelOf(::LessonInfoViewModel)
        viewModelOf(::TeacherInfoViewModel)
        viewModelOf(::GroupInfoViewModel)
        viewModelOf(::PlaceInfoViewModel)
    }

    val screens = screens {
        addScreen<ScheduleInfoScreens.LessonInfo> {
            LessonInfoScreen(
                lessonInfo = getArg(ScheduleInfoScreens.LessonInfo::lessonInfo.name),
            )
        }

        addScreen<ScheduleInfoScreens.TeacherInfo> {
            TeacherInfoScreen(
                id = getArg(ScheduleInfoScreens.TeacherInfo::id.name),
            )
        }

        addScreen<ScheduleInfoScreens.GroupInfo> {
            GroupInfoScreen(
                id = getArg(ScheduleInfoScreens.GroupInfo::id.name),
            )
        }

        addScreen<ScheduleInfoScreens.PlaceInfo> {
            PlaceInfoScreen(
                id = getArg(ScheduleInfoScreens.PlaceInfo::id.name),
            )
        }
    }
}
