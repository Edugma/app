package io.edugma.features.account.students

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.edugma.domain.account.model.Student
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.repository.PeoplesRepository
import io.edugma.features.account.teachers.TeachersPagingSource
import io.edugma.features.base.core.mvi.BaseViewModel
import kotlinx.coroutines.flow.Flow

class StudentsViewModel(private val repository: PeoplesRepository) :
    BaseViewModel<StudentsState>(StudentsState()) {

    companion object {
        private const val PAGE_SIZE = 10
    }

    init {
        load()
    }

    private fun requestPagingData(name: String) = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        StudentsPagingSource(repository, name, PAGE_SIZE)
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

}

data class StudentsState(
    val pagingData: Flow<PagingData<Student>>? = null,
    val name: String = "",
)