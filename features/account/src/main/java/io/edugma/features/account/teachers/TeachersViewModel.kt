package io.edugma.features.account.teachers

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.repository.PeoplesRepository
import io.edugma.features.base.core.mvi.BaseViewModel
import kotlinx.coroutines.flow.*

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
        mutateState {
            state = state.copy(pagingData = flow)
        }
    }

    fun setName(name: String) {
        mutateState {
            state = state.copy(name = name)
        }
    }

    fun openSearch() {
        mutateState {
            state = state.copy(bottomType = BottomType.Search, selectedEntity = null)
        }
    }

    fun openTeacher(teacher: Teacher) {
        mutateState {
            state = state.copy(bottomType = BottomType.Teacher, selectedEntity = teacher)
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
