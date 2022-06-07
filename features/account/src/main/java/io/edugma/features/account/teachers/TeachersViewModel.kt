package io.edugma.features.account.teachers

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.repository.PeoplesRepository
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.domain.base.utils.execute
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.BaseViewModelFull
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TeachersViewModel(private val repository: PeoplesRepository) :
    BaseViewModel<TeachersState>(TeachersState()) {

    companion object {
        private const val PAGE_SIZE = 30
    }

    private var pagingJob: Job? = null

    init {
        load()
    }

    private fun requestPagingData(name: String) = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        TeachersPagingSource(repository, name, PAGE_SIZE)
    }

    fun load(name: String = "") {
        val flow = requestPagingData(name)
            .flow
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

data class TeachersState(
    val pagingData: Flow<PagingData<Teacher>>? = null,
    val name: String = "",
)