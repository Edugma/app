package io.edugma.features.account.people.presentation

import androidx.compose.runtime.Immutable
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.arch.pagination.PaginationState
import io.edugma.core.arch.pagination.PaginationStateEnum
import io.edugma.core.arch.pagination.PagingViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.navigation.schedule.ScheduleInfoScreens
import io.edugma.features.account.domain.model.peoples.Person
import io.edugma.features.account.domain.repository.PeoplesRepository
import io.edugma.features.account.people.PeopleScreenType
import io.edugma.features.account.people.common.utlis.convertAndShare
import kotlinx.coroutines.flow.map

class PeopleViewModel(
    private val repository: PeoplesRepository,
    private val externalRouter: ExternalRouter,
    private val pagingViewModel: PagingViewModel<Person>,
) : BaseViewModel<StudentsState>(StudentsState()) {

    init {
        pagingViewModel.init(
            parentViewModel = this,
            initialState = state.paginationState,
            stateFlow = stateFlow.map { it.paginationState },
            setState = { newState { copy(paginationState = it) } },
        )
        pagingViewModel.request = {
            repository.getPeople(
                url = state.type!!.url,
                query = state.name,
                page = state.paginationState.nextPage,
                limit = state.paginationState.pageSize,
            )
        }
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

    fun selectPerson(student: Person) {
        newState {
            copy(selectedPerson = student, bottomType = BottomSheetType.Person)
        }
    }

    fun searchRequest() {
        pagingViewModel.resetAndLoad()
    }

    fun onArgs(type: PeopleScreenType) {
        newState {
            copy(
                type = type,
            )
        }
        pagingViewModel.resetAndLoad()
    }

    fun openSchedule() {
        state.selectedPerson?.id?.let {
            router.navigateTo(ScheduleInfoScreens.TeacherInfo(it))
        }
    }
}

@Immutable
data class StudentsState(
    val type: PeopleScreenType? = null,
    val name: String = "",
    val bottomType: BottomSheetType = BottomSheetType.Filter,
    val selectedPerson: Person? = null,
    val paginationState: PaginationState<Person> = PaginationState.empty(),
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
    Filter, Person
}
