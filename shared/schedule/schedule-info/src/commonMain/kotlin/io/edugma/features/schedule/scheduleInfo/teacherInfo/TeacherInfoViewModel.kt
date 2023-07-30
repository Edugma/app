package io.edugma.features.schedule.scheduleInfo.teacherInfo

import io.edugma.core.api.utils.onFailure
import io.edugma.core.api.utils.onSuccess
import io.edugma.core.arch.mvi.updateState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.arch.mvi.viewmodel.prop
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import io.edugma.features.schedule.domain.repository.ScheduleInfoRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

class TeacherInfoViewModel(
    private val repository: ScheduleInfoRepository,
) : BaseViewModel<TeacherInfoState>(TeacherInfoState()) {
    init {
        launchCoroutine {
            state.prop { id }.filterNotNull().collect {
                repository.getTeacherInfo(it)
                    .onSuccess {
                        updateState {
                            copy(
                                teacherInfo = it,
                                scheduleSource = ScheduleSource(
                                    type = ScheduleSources.Teacher,
                                    key = it.id,
                                ),
                            )
                        }
                    }.onFailure {
                    }.collect()
            }
        }
    }

    fun setId(id: String) {
        updateState {
            copy(id = id)
        }
    }

    fun onTabSelected(tab: TeacherInfoTabs) {
        updateState {
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
