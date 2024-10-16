package com.edugma.features.schedule.scheduleInfo.teacherInfo

import com.edugma.core.arch.mvi.viewmodel.FeatureLogic2
import com.edugma.features.schedule.domain.model.source.ScheduleSource
import com.edugma.features.schedule.domain.model.teacher.TeacherInfo
import com.edugma.features.schedule.domain.repository.ScheduleInfoRepository

class TeacherInfoViewModel(
    private val repository: ScheduleInfoRepository,
) : FeatureLogic2<TeacherInfoState>() {
    override fun initialState(): TeacherInfoState {
        return TeacherInfoState()
    }

    override fun onCreate() {
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

    fun exit() {
        scheduleRouter.back()
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
