package io.edugma.features.schedule.schedule_info

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.ScheduleScreens
import io.edugma.features.base.navigation.schedule.ScheduleInfoScreens
import io.edugma.features.schedule.schedule_info.group_info.GroupInfoScreen
import io.edugma.features.schedule.schedule_info.group_info.GroupInfoViewModel
import io.edugma.features.schedule.schedule_info.lesson_info.LessonInfoScreen
import io.edugma.features.schedule.schedule_info.lesson_info.LessonInfoViewModel
import io.edugma.features.schedule.schedule_info.place_info.PlaceInfoScreen
import io.edugma.features.schedule.schedule_info.place_info.PlaceInfoViewModel
import io.edugma.features.schedule.schedule_info.teacher_info.TeacherInfoScreen
import io.edugma.features.schedule.schedule_info.teacher_info.TeacherInfoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
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
                lessonInfo = getArg(ScheduleInfoScreens.LessonInfo::lessonInfo.name)
            )
        }

        addScreen<ScheduleInfoScreens.TeacherInfo> {
            TeacherInfoScreen(
                id = getArg(ScheduleInfoScreens.TeacherInfo::id.name)
            )
        }

        addScreen<ScheduleInfoScreens.GroupInfo> {
            GroupInfoScreen(
                id = getArg(ScheduleInfoScreens.GroupInfo::id.name)
            )
        }

        addScreen<ScheduleInfoScreens.PlaceInfo> {
            PlaceInfoScreen(
                id = getArg(ScheduleInfoScreens.PlaceInfo::id.name)
            )
        }
    }
}