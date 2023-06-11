package io.edugma.features.schedule.scheduleInfo

import io.edugma.core.navigation.schedule.ScheduleInfoScreens
import io.edugma.features.schedule.scheduleInfo.groupInfo.GroupInfoScreen
import io.edugma.features.schedule.scheduleInfo.groupInfo.GroupInfoViewModel
import io.edugma.features.schedule.scheduleInfo.lessonInfo.LessonInfoScreen
import io.edugma.features.schedule.scheduleInfo.lessonInfo.LessonInfoViewModel
import io.edugma.features.schedule.scheduleInfo.placeInfo.PlaceInfoScreen
import io.edugma.features.schedule.scheduleInfo.placeInfo.PlaceInfoViewModel
import io.edugma.features.schedule.scheduleInfo.teacherInfo.TeacherInfoScreen
import io.edugma.features.schedule.scheduleInfo.teacherInfo.TeacherInfoViewModel
import io.edugma.navigation.core.graph.screenModule
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object ScheduleInfoFeatureModule {
    val deps = module {
        viewModelOf(::LessonInfoViewModel)
        viewModelOf(::TeacherInfoViewModel)
        viewModelOf(::GroupInfoViewModel)
        viewModelOf(::PlaceInfoViewModel)
    }

    val screens = screenModule {
        screen(ScheduleInfoScreens.LessonInfo) {
            LessonInfoScreen(
                lessonInfo = Json.decodeFromString(screen.lessonInfo.get()),
            )
        }

        screen(ScheduleInfoScreens.TeacherInfo) {
            TeacherInfoScreen(
                id = screen.id.get(),
            )
        }

        screen(ScheduleInfoScreens.GroupInfo) {
            GroupInfoScreen(
                id = screen.id.get(),
            )
        }

        screen(ScheduleInfoScreens.PlaceInfo) {
            PlaceInfoScreen(
                id = screen.id.get(),
            )
        }
    }
}
