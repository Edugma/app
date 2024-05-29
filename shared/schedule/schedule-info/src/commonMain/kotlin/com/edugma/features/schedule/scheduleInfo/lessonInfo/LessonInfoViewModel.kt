package com.edugma.features.schedule.scheduleInfo.lessonInfo

import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.viewmodel.BaseViewModel
import com.edugma.core.navigation.schedule.ScheduleInfoScreens
import com.edugma.features.schedule.domain.model.lesson.LessonEvent
import com.edugma.features.schedule.domain.model.teacher.TeacherInfo
import com.edugma.features.schedule.domain.usecase.ScheduleUseCase

class LessonInfoViewModel(
    private val scheduleUseCase: ScheduleUseCase,
) : BaseViewModel<LessonInfoState>(LessonInfoState()) {

    init {
//        launchCoroutine {
//            stateFlow.prop { lessonInfo }
//                .collectLatest {
//                    val teachers = it?.lesson?.teachers?.map {
//                        scheduleUseCase.getTeacher(it.id).first()
//                    }?.filterNotNull() ?: emptyList()
//
//                    newState {
//                        copy(
//                            teachers = teachers,
//                        )
//                    }
//                }
//        }
    }

    fun onLessonInfo(lessonInfo: LessonEvent?) {
        newState {
            copy(lessonInfo = lessonInfo)
        }
    }

    fun onTeacherClick(id: String) {
        scheduleRouter.navigateTo(ScheduleInfoScreens.TeacherInfo(id))
    }

    fun onGroupClick(id: String) {
        scheduleRouter.navigateTo(ScheduleInfoScreens.GroupInfo(id))
    }

    fun onPlaceClick(id: String) {
        scheduleRouter.navigateTo(ScheduleInfoScreens.PlaceInfo(id))
    }

    fun exit() {
        scheduleRouter.back()
    }
}

data class LessonInfoState(
    val lessonInfo: LessonEvent? = null,
    val teachers: List<TeacherInfo> = emptyList(),
)
