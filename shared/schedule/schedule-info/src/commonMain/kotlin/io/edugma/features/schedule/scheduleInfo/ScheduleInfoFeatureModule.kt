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
import io.edugma.navigation.core.compose.rememberNavArgs
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.graph.composeScreen
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
