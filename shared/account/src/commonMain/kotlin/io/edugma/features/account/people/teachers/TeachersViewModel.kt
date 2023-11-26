package io.edugma.features.account.people.teachers

import androidx.compose.runtime.Immutable
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.arch.pagination.PaginationState
import io.edugma.core.arch.pagination.PaginationStateEnum
import io.edugma.core.arch.pagination.PagingViewModel
import io.edugma.core.navigation.schedule.ScheduleInfoScreens
import io.edugma.features.account.domain.model.Teacher
import io.edugma.features.account.domain.repository.PeoplesRepository
import kotlinx.coroutines.flow.map

class TeachersViewModel(
    private val repository: PeoplesRepository,
    private val pagingViewModel: PagingViewModel<Teacher>,
) : BaseViewModel<TeachersState>(TeachersState()) {

    init {
        pagingViewModel.init(
            parentViewModel = this,
            initialState = state.paginationState,
            stateFlow = stateFlow.map { it.paginationState },
            setState = { newState { copy(paginationState = it) } },
        )
        pagingViewModel.request = {
            repository.getTeachers(
                query = state.name,
                page = state.paginationState.nextPage,
                limit = state.paginationState.pageSize,
            )
        }
        pagingViewModel.initLoad()
    }

    fun loadNextPage() {
        pagingViewModel.loadNextPage()
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

    fun onSearch() {
        pagingViewModel.initLoad()
    }
}

@Immutable
data class TeachersState(
    val name: String = "",
    val bottomType: BottomType = BottomType.Search,
    val selectedEntity: Teacher? = null,
    val paginationState: PaginationState<Teacher> = PaginationState.empty(),
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

enum class BottomType {
    Teacher, Search
}
