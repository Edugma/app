package io.edugma.features.account.people.teachers

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.schedule.ScheduleInfoScreens
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.Typed2Listener
import io.edugma.features.account.domain.model.Teacher
import io.edugma.features.account.domain.model.student.Student
import io.edugma.features.account.domain.repository.PeoplesRepository
import io.edugma.features.account.domain.usecase.PaginationState
import io.edugma.features.account.domain.usecase.PagingUseCase
import io.edugma.features.account.domain.usecase.PagingViewModel
import io.edugma.features.account.people.teachers.TeachersViewModel.Companion.INIT_NAME
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

class TeachersViewModel(private val repository: PeoplesRepository) :
    BaseViewModel<TeachersState>(TeachersState()), PagingViewModel<Teacher> {

    companion object {
        private const val PAGE_SIZE = 100
        const val INIT_NAME = ""
    }

    override lateinit var pagingUC: PagingUseCase<Teacher>

    override var pagingStateJob: Job? = null
    override var loadingJob: Job? = null
    override val onDataLoaded: Typed2Listener<List<Teacher>, Boolean> =
        { teachers, fromBegin -> addTeachers(teachers, fromBegin) }
    override val onLoadingStateChange: Typed1Listener<PaginationState> = ::setLoadingState
    override val onError: Typed1Listener<Throwable>? = null

    init {
        load()
    }

    fun load(name: String = state.name) {
        updateUc { repository.getTeachersResult(name, 1, PAGE_SIZE) }
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

    fun nextPage() {
        loadNext()
    }

    private fun addTeachers(teachers: List<Teacher>, fromBegin: Boolean = true) {
        newState {
            val newTeachers = if (fromBegin) teachers else this.teachers.orEmpty() + teachers
            copy(teachers = newTeachers)
        }
    }

    private fun setLoadingState(loadingState: PaginationState) {
        newState {
            copy(loadingState = loadingState)
        }
    }
}

data class TeachersState(
    val teachers: List<Teacher>? = null,
    val loadingState: PaginationState = PaginationState.NotLoading,
    val name: String = INIT_NAME,
    val bottomType: BottomType = BottomType.Search,
    val selectedEntity: Teacher? = null,
) {
    val isNothingFound
        get() = loadingState == PaginationState.End && teachers.isNullOrEmpty()

    val isFullscreenError
        get() = teachers.isNullOrEmpty() && loadingState == PaginationState.Error

    val placeholders
        get() = teachers.isNullOrEmpty() && (loadingState == PaginationState.NotLoading || loadingState == PaginationState.Loading)
}

enum class BottomType {
    Teacher, Search
}
