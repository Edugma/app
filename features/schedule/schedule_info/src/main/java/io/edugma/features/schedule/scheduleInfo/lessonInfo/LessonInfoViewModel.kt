package io.edugma.features.schedule.scheduleInfo.lessonInfo

import io.edugma.core.navigation.schedule.ScheduleInfoScreens
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.prop
import io.edugma.features.schedule.domain.model.lesson.LessonInfo
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first

class LessonInfoViewModel(
    private val scheduleUseCase: ScheduleUseCase,
) : BaseViewModel<LessonInfoState>(LessonInfoState()) {

    init {
        launchCoroutine {
            state.prop { lessonInfo }
                .collectLatest {
                    val teachers = it?.lesson?.teachers?.map {
                        scheduleUseCase.getTeacher(it.id).first()
                    }?.filterNotNull() ?: emptyList()

                    mutateState {
                        state = state.copy(
                            teachers = teachers,
                        )
                    }
                }
        }
    }

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
    val lessonInfo: LessonInfo? = null,
    val teachers: List<TeacherInfo> = emptyList(),
)
