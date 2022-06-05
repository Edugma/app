package io.edugma.features.schedule.schedule_info.teacher_info

import androidx.lifecycle.viewModelScope
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.domain.schedule.model.teacher.TeacherInfo
import io.edugma.domain.schedule.repository.ScheduleInfoRepository
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.prop
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class TeacherInfoViewModel(
    private val repository: ScheduleInfoRepository
) : BaseViewModel<TeacherInfoState>(TeacherInfoState()) {
    init {
        viewModelScope.launch {
            state.prop { id }.filterNotNull().collect {
                repository.getTeacherInfo(it)
                    .onSuccess {
                        mutateState {
                            state = state.copy(teacherInfo = it)
                        }
                    }.onFailure {

                    }.collect()
            }
        }
    }

    fun setId(id: String) {
        mutateState {
            state = state.copy(id = id)
        }
    }

    fun onTabSelected(tab: TeacherInfoTabs) {
        mutateState {
            state = state.copy(selectedTab = tab)
        }
    }
}

data class TeacherInfoState(
    val id: String? = null,
    val teacherInfo: TeacherInfo? = null,
    val tabs: List<TeacherInfoTabs> = TeacherInfoTabs.values().toList(),
    val selectedTab: TeacherInfoTabs = TeacherInfoTabs.Schedule,
)

enum class TeacherInfoTabs {
    Schedule
}