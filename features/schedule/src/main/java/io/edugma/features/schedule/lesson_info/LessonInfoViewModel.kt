package io.edugma.features.schedule.lesson_info

import io.edugma.domain.schedule.model.lesson.LessonInfo
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.navigation.schedule.ScheduleInfoScreens

class LessonInfoViewModel : BaseViewModel<LessonInfoState>(LessonInfoState()) {
    fun onLessonInfo(lessonInfo: LessonInfo?) {
        mutateState {
            state = state.copy(lessonInfo = lessonInfo)
        }
    }

    fun onTeacherClick(id: String) {
        router.navigateTo(ScheduleInfoScreens.TeacherInfo(id))
    }

    fun onGroupClick(id: String) {
        router.navigateTo(ScheduleInfoScreens.GroupInfo(id))
    }

    fun onPlaceClick(id: String) {
        router.navigateTo(ScheduleInfoScreens.PlaceInfo(id))
    }
}

data class LessonInfoState(
    val lessonInfo: LessonInfo? = null
)