package io.edugma.features.account.people.students

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.domain.account.model.student.Student
import io.edugma.domain.account.repository.PeoplesRepository
import kotlinx.coroutines.flow.collect

class StudentsViewModel(
    private val repository: PeoplesRepository,
    private val externalRouter: ExternalRouter,
) : BaseViewModel<StudentsState>(StudentsState()) {

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
        launchCoroutine {
            requestPagingData(name)
                .flow
                .cachedIn(viewModelScope)
                .collect {
                    mutateState {
                        state = state.copy(pagingData = it)
                    }
                }
        }
    }

    fun setName(name: String) {
        mutateState {
            state = state.copy(name = name)
        }
    }

    fun onShare() {
//        viewModelScope.launch {
//            val students = state.value.pagingData
//            students?.let { students ->
//                externalRouter.share(
//                    students.convertAndShare()
//                )
//            }
//        }
    }

    fun selectFilter() {
        mutateState {
            state = state.copy(bottomType = BottomSheetType.Filter)
        }
    }

    fun selectStudent(student: Student) {
        mutateState {
            state = state.copy(selectedStudent = student, bottomType = BottomSheetType.Student)
        }
    }
}

data class StudentsState(
    val pagingData: PagingData<Student>? = null,
    val name: String = "",
    val bottomType: BottomSheetType = BottomSheetType.Filter,
    val selectedStudent: Student? = null,
)

enum class BottomSheetType {
    Filter, Student
}
