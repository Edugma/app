package com.edugma.features.schedule.scheduleInfo

import com.edugma.core.navigation.schedule.ScheduleInfoScreens
import com.edugma.features.schedule.scheduleInfo.groupInfo.GroupInfoScreen
import com.edugma.features.schedule.scheduleInfo.groupInfo.GroupInfoViewModel
import com.edugma.features.schedule.scheduleInfo.lessonInfo.LessonInfoScreen
import com.edugma.features.schedule.scheduleInfo.lessonInfo.LessonInfoViewModel
import com.edugma.features.schedule.scheduleInfo.placeInfo.PlaceInfoScreen
import com.edugma.features.schedule.scheduleInfo.placeInfo.PlaceInfoViewModel
import com.edugma.features.schedule.scheduleInfo.teacherInfo.TeacherInfoScreen
import com.edugma.features.schedule.scheduleInfo.teacherInfo.TeacherInfoViewModel
import com.edugma.navigation.core.compose.rememberNavArgs
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleInfoFeatureModule {
    val deps = module {
        factoryOf(::LessonInfoViewModel)
        factoryOf(::TeacherInfoViewModel)
        factoryOf(::GroupInfoViewModel)
        factoryOf(::PlaceInfoViewModel)
    }

    fun NavGraphBuilder.screens() {
        composeScreen(ScheduleInfoScreens.LessonInfo) {
            val args = rememberNavArgs(ScheduleInfoScreens.LessonInfo)

            LessonInfoScreen(
                lessonInfo = Json.decodeFromString(args { destination.lessonInfo.get() }),
            )
        }

        composeScreen(ScheduleInfoScreens.TeacherInfo) {
            val args = rememberNavArgs(ScheduleInfoScreens.TeacherInfo)

            TeacherInfoScreen(
                id = args { destination.id.get() },
            )
        }

        composeScreen(ScheduleInfoScreens.GroupInfo) {
            val args = rememberNavArgs(ScheduleInfoScreens.GroupInfo)

            GroupInfoScreen(
                id = args { destination.id.get() },
            )
        }

        composeScreen(ScheduleInfoScreens.PlaceInfo) {
            val args = rememberNavArgs(ScheduleInfoScreens.PlaceInfo)

            PlaceInfoScreen(
                id = args { destination.id.get() },
            )
        }
    }
}
