package io.edugma.features.account.people.presentation

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
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
) : BaseActionViewModel<PeopleUiState, PeopleAction>(PeopleUiState()) {

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

    override fun processAction(action: PeopleAction) {
        when (action) {
            PeopleAction.OnRefresh -> pagingViewModel.resetAndLoad()
            PeopleAction.OnLoadNextPage -> pagingViewModel.loadNextPage()
            is PeopleAction.OnQuery -> onQuery(action.query)
        }
    }

    private fun onQuery(name: String) {
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

    fun onSelectFilter() {
        newState {
            copy(bottomType = BottomSheetType.Filter)
        }
    }

    fun onSelectPerson(student: Person) {
        newState {
            copy(selectedPerson = student, bottomType = BottomSheetType.Person)
        }
    }

    fun onSearch() {
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
            accountRouter.navigateTo(ScheduleInfoScreens.TeacherInfo(it))
        }
    }

    fun exit() {
        accountRouter.back()
    }
}

enum class BottomSheetType {
    Filter, Person
}
