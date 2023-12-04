package io.edugma.features.account.people.students

import androidx.compose.runtime.Immutable
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.arch.pagination.PaginationState
import io.edugma.core.arch.pagination.PaginationStateEnum
import io.edugma.core.arch.pagination.PagingViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.features.account.domain.model.student.Student
import io.edugma.features.account.domain.repository.PeoplesRepository
import io.edugma.features.account.people.common.utlis.convertAndShare
import kotlinx.coroutines.flow.map

class StudentsViewModel(
    private val repository: PeoplesRepository,
    private val externalRouter: ExternalRouter,
    private val pagingViewModel: PagingViewModel<Student>,
) : BaseViewModel<StudentsState>(StudentsState()) {

    init {
        pagingViewModel.init(
            parentViewModel = this,
            initialState = state.paginationState,
            stateFlow = stateFlow.map { it.paginationState },
            setState = { newState { copy(paginationState = it) } },
        )
        pagingViewModel.request = {
            repository.getStudents(
                query = state.name,
                page = state.paginationState.nextPage,
                limit = state.paginationState.pageSize,
            )
        }
        pagingViewModel.resetAndLoad()
    }

    fun loadNextPage() {
        pagingViewModel.loadNextPage()
    }

    fun setName(name: String) {
        newState {
            copy(name = name)
        }
    }

    fun onShare() {
        launchCoroutine {
            state.paginationState.items
                .convertAndShare()
                .let(externalRouter::share)
        }
    }

    fun selectFilter() {
        newState {
            copy(bottomType = BottomSheetType.Filter)
        }
    }

    fun selectStudent(student: Student) {
        newState {
            copy(selectedStudent = student, bottomType = BottomSheetType.Student)
        }
    }

    fun searchRequest() {
        pagingViewModel.resetAndLoad()
    }
}

@Immutable
data class StudentsState(
    val name: String = "",
    val bottomType: BottomSheetType = BottomSheetType.Filter,
    val selectedStudent: Student? = null,
    val paginationState: PaginationState<Student> = PaginationState.empty(),
) {
    val isNothingFound
        get() = paginationState.isEnd() && paginationState.items.isEmpty()

    val isFullscreenError
        get() = paginationState.items.isEmpty() && paginationState.isError()

    val placeholders
        get() = paginationState.items.isEmpty() &&
            (
                paginationState.enum == PaginationStateEnum.NotLoading ||
                    paginationState.enum == PaginationStateEnum.Loading
                )
}

enum class BottomSheetType {
    Filter, Student
}
