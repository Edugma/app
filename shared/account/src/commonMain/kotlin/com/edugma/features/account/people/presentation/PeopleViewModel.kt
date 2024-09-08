package com.edugma.features.account.people.presentation

import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import com.edugma.core.arch.pagination.PagingViewModel
import com.edugma.core.navigation.core.router.external.ExternalRouter
import com.edugma.core.navigation.schedule.ScheduleInfoScreens
import com.edugma.features.account.domain.model.peoples.Person
import com.edugma.features.account.domain.repository.PeoplesRepository
import com.edugma.features.account.people.PeopleScreenType
import com.edugma.features.account.people.common.utlis.convertAndShare
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
            PeopleAction.OnRefresh -> pagingViewModel.refresh()
            PeopleAction.OnLoadNextPage -> pagingViewModel.loadNextPage()
            is PeopleAction.OnQuery -> onQuery(action.query)
            is PeopleAction.OnArgs -> onArgs(action.type)
            PeopleAction.OnSearchClick -> onSearch()
            PeopleAction.Back -> exit()
            PeopleAction.OnSelectFilter -> onSelectFilter()
            is PeopleAction.OnSelectPerson -> onSelectPerson(action.person)
            PeopleAction.OnShareClick -> onShare()
            PeopleAction.OnOpenSchedule -> openSchedule()
            PeopleAction.OnBottomSheetClosed -> newState { copy(showBottomSheet = false) }
        }
    }

    private fun onQuery(name: String) {
        newState {
            copy(name = name)
        }
    }

    private fun onShare() {
        launchCoroutine {
            state.paginationState.items
                .convertAndShare()
                .let(externalRouter::share)
        }
    }

    private fun onSelectFilter() {
        newState {
            copy(
                bottomType = BottomSheetType.Filter,
                showBottomSheet = true,
            )
        }
    }

    private fun onSelectPerson(person: Person) {
        newState {
            copy(
                selectedPerson = person,
                bottomType = BottomSheetType.Person,
                showBottomSheet = true,
            )
        }
    }

    private fun onSearch() {
        newState {
            copy(showBottomSheet = false)
        }
        pagingViewModel.resetAndLoad()
    }

    private fun onArgs(type: PeopleScreenType) {
        newState {
            copy(
                type = type,
            )
        }
        pagingViewModel.resetAndLoad()
    }

    private fun openSchedule() {
        state.selectedPerson?.id?.let {
            accountRouter.navigateTo(ScheduleInfoScreens.TeacherInfo(it))
        }
    }

    private fun exit() {
        accountRouter.back()
    }
}

enum class BottomSheetType {
    Filter,
    Person
}
