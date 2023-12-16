package io.edugma.features.schedule.scheduleInfo.teacherInfo

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import io.edugma.features.schedule.domain.repository.ScheduleInfoRepository

class TeacherInfoViewModel(
    private val repository: ScheduleInfoRepository,
) : BaseViewModel<TeacherInfoState>(TeacherInfoState()) {
    init {
        // TODO
//        launchCoroutine {
//            stateFlow.prop { id }.filterNotNull().collect {
//                repository.getTeacherInfo(it)
//                    .onSuccess {
//                        newState {
//                            copy(
//                                teacherInfo = it,
//                                scheduleSource = ScheduleSource(
//                                    type = "teacher",
//                                    key = it.id,
//                                ),
//                            )
//                        }
//                    }.onFailure {
//                    }.collect()
//            }
//        }
    }

    fun setId(id: String) {
        newState {
            copy(id = id)
        }
    }

    fun onTabSelected(tab: TeacherInfoTabs) {
        newState {
            copy(selectedTab = tab)
        }
    }
}

data class TeacherInfoState(
    val id: String? = null,
    val teacherInfo: TeacherInfo? = null,
    val tabs: List<TeacherInfoTabs> = listOf(TeacherInfoTabs.Schedule),
    val selectedTab: TeacherInfoTabs = TeacherInfoTabs.Schedule,
    val scheduleSource: ScheduleSource? = null,
)

enum class TeacherInfoTabs {
    Schedule,
}
