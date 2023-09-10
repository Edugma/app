package io.edugma.features.account.people.students

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.Typed2Listener
import io.edugma.features.account.domain.model.student.Student
import io.edugma.features.account.domain.repository.PeoplesRepository
import io.edugma.features.account.domain.usecase.PaginationState
import io.edugma.features.account.domain.usecase.PagingUseCase
import io.edugma.features.account.domain.usecase.PagingViewModel
import io.edugma.features.account.people.common.utlis.convertAndShare
import io.edugma.features.account.people.students.StudentsViewModel.Companion.INIT_NAME
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StudentsViewModel(
    private val repository: PeoplesRepository,
    private val externalRouter: ExternalRouter,
) : BaseViewModel<StudentsState>(StudentsState()), PagingViewModel<Student> {

    companion object {
        private const val PAGE_SIZE = 100
        const val INIT_NAME = ""
    }

    override lateinit var pagingUC: PagingUseCase<Student>

    override var pagingStateJob: Job? = null
    override var loadingJob: Job? = null
    override val onDataLoaded: Typed2Listener<List<Student>, Boolean> =
        { students, fromBegin -> addStudents(students, fromBegin) }
    override val onLoadingStateChange: Typed1Listener<PaginationState> = ::setPagingState
    override val onError: Typed1Listener<Throwable>? = null

    init {
        updateStudentsUC(INIT_NAME)
    }

    fun setName(name: String) {
        newState {
            copy(name = name)
        }
    }

    fun onShare() {
        viewModelScope.launch {
            state.students
                ?.convertAndShare()
                ?.let(externalRouter::share)
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

    fun loadNextPage() {
        loadNext()
    }

    fun searchRequest() {
        updateStudentsUC(state.name)
    }

    private fun updateStudentsUC(name: String) {
        updateUc { repository.getStudentsResult(name, it, PAGE_SIZE) }
    }

    private fun addStudents(students: List<Student>, fromBegin: Boolean = true) {
        newState {
            val newStudents = if (fromBegin) students else this.students.orEmpty() + students
            copy(students = newStudents)
        }
    }

    private fun setPagingState(paginationState: PaginationState) {
        newState {
            copy(loadingState = paginationState)
        }
    }
}

data class StudentsState(
    val students: List<Student>? = null,
    val loadingState: PaginationState = PaginationState.NotLoading,
    val name: String = INIT_NAME,
    val bottomType: BottomSheetType = BottomSheetType.Filter,
    val selectedStudent: Student? = null,
) {
    val isNothingFound
        get() = loadingState == PaginationState.End && students.isNullOrEmpty()

    val isFullscreenError
        get() = students.isNullOrEmpty() && loadingState == PaginationState.Error

    val placeholders
        get() = students.isNullOrEmpty() && (loadingState == PaginationState.NotLoading || loadingState == PaginationState.Loading)
}

enum class BottomSheetType {
    Filter, Student
}
