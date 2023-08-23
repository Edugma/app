package io.edugma.features.account.people.teachers

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.schedule.ScheduleInfoScreens
import io.edugma.features.account.domain.model.Teacher
import io.edugma.features.account.domain.repository.PeoplesRepository
import kotlinx.coroutines.flow.Flow

class TeachersViewModel(private val repository: PeoplesRepository) :
    BaseViewModel<TeachersState>(TeachersState()) {

    companion object {
        private const val PAGE_SIZE = 10
    }

    init {
        load()
    }

    private fun requestPagingData(name: String) = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        TeachersPagingSource(repository, name, PAGE_SIZE)
    }

    fun load(name: String = "") {
        val flow = requestPagingData(name)
            .flow
            .cachedIn(viewModelScope)
        newState {
            copy(pagingData = flow)
        }
    }

    fun setName(name: String) {
        newState {
            copy(name = name)
        }
    }

    fun openSearch() {
        newState {
            copy(bottomType = BottomType.Search, selectedEntity = null)
        }
    }

    fun openTeacher(teacher: Teacher) {
        newState {
            copy(bottomType = BottomType.Teacher, selectedEntity = teacher)
        }
    }

    fun openTeacherSchedule() {
        stateFlow.value.selectedEntity?.id?.let {
            router.navigateTo(ScheduleInfoScreens.TeacherInfo(it))
        }
    }
}

data class TeachersState(
    val pagingData: Flow<PagingData<Teacher>>? = null,
    val name: String = "",
    val bottomType: BottomType = BottomType.Search,
    val selectedEntity: Teacher? = null,
)

enum class BottomType {
    Teacher, Search
}
