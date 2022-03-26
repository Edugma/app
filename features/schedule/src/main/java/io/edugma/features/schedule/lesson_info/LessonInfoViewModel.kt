package io.edugma.features.schedule.lesson_info

import io.edugma.domain.schedule.model.lesson.LessonInfo
import io.edugma.features.base.core.mvi.BaseViewModel

class LessonInfoViewModel : BaseViewModel<LessonInfoState>(LessonInfoState()) {
    fun onLessonInfo(lessonInfo: LessonInfo?) {
        mutateState {
            state = state.copy(lessonInfo = lessonInfo)
        }
    }
}

data class LessonInfoState(
    val lessonInfo: LessonInfo? = null
)