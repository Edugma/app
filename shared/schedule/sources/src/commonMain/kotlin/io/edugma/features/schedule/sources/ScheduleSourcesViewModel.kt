package io.edugma.features.schedule.sources

import io.edugma.core.api.model.PagingDto
import io.edugma.core.api.model.map
import io.edugma.core.arch.mvi.delegate.debounce
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.arch.pagination.PagingViewModel
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSourceType
import io.edugma.features.schedule.domain.usecase.ScheduleSourcesUseCase
import io.edugma.features.schedule.sources.model.ScheduleSourceUiModel
import io.edugma.features.schedule.sources.model.toDtoModel
import io.edugma.features.schedule.sources.model.toUiModel
import kotlinx.coroutines.flow.map

class ScheduleSourcesViewModel(
    private val useCase: ScheduleSourcesUseCase,
    private val pagingViewModel: PagingViewModel<ScheduleSourceUiModel>,
) : BaseActionViewModel<ScheduleSourceUiState, ScheduleSourcesAction>(ScheduleSourceUiState()) {
    init {
        pagingViewModel.init(
            parentViewModel = this,
            initialState = state.paginationState,
            stateFlow = stateFlow.map { it.paginationState },
            setState = { newState { copy(paginationState = it) } },
        )
        pagingViewModel.request = {
            val selectedTab = state.selectedTab
            if (selectedTab == null) {
                PagingDto.empty()
            } else {
                val sources = useCase.getScheduleSources(
                    type = selectedTab,
                    query = state.query,
                    page = state.paginationState.nextPage,
                    limit = state.paginationState.pageSize,
                )
                // TODO для favorite и complex
                val favoriteSources = useCase.getFavoriteSources()
                sources.map {
                    it.toUiModel(isFavorite = it in favoriteSources)
                }
            }
        }

        launchCoroutine(
            onError = {
                newState { setTabs(tabs = emptyList()) }
            },
        ) {
            val tabs = useCase.getSourceTypes()
            val oldSelectedTab = state.selectedTab
            newState { setTabs(tabs = tabs) }
            if (oldSelectedTab != state.selectedTab) {
                pagingViewModel.resetAndLoad()
            }
        }
    }

    private val queryDebounce = debounce()

    override fun onAction(action: ScheduleSourcesAction) {
        when (action) {
            ScheduleSourcesAction.OnLoadPage -> {
                pagingViewModel.loadNextPage()
            }

            is ScheduleSourcesAction.OnAddToFavorite -> onAddFavorite(action.source)
            is ScheduleSourcesAction.OnSourceSelected -> onSelectSource(action.source)
            is ScheduleSourcesAction.OnDeleteFromFavorite -> onDeleteFavorite(action.source)
            is ScheduleSourcesAction.OnSourceClicked -> onSourceClick(action.source)
            is ScheduleSourcesAction.OnTabSelected -> onSelectTab(action.type)
            ScheduleSourcesAction.OnRefresh -> pagingViewModel.resetAndLoad()
        }
    }

    fun onBottomSheetClosed() {
        newState {
            copy(showBottomSheet = false)
        }
    }

    private fun onSourceClick(source: ScheduleSourceUiModel) {
        newState {
            copy(
                selectedSource = source,
                showBottomSheet = true,
            )
        }
    }

    private fun onSelectTab(selectedTab: ScheduleSourceType) {
        val previousTab = state.selectedTab
        newState {
            copy(selectedTab = selectedTab)
        }
        if (previousTab != selectedTab) {
            if (selectedTab.id != ScheduleSourceType.COMPLEX) {
                pagingViewModel.resetAndLoad()
            }
        }
    }

    private fun onSelectSource(source: ScheduleSourceUiModel) {
        launchCoroutine {
            useCase.setSelectedSource(source.toDtoModel())
            router.back()
        }
    }

    private fun onAddFavorite(source: ScheduleSourceUiModel) {
        launchCoroutine {
            useCase.addFavoriteSource(source.toDtoModel())
        }
        pagingViewModel.resetAndLoad()
    }

    private fun onDeleteFavorite(source: ScheduleSourceUiModel) {
        launchCoroutine {
            useCase.deleteFavoriteSource(source.id)
        }
        pagingViewModel.resetAndLoad()
    }

    fun onQueryChange(query: String) {
        newState {
            setQuery(query = query)
        }
        queryDebounce.launchCoroutine {
            pagingViewModel.resetAndLoad()
        }
    }

    fun onApplyComplexSearch() {
        launchCoroutine {
            useCase.setSelectedSource(
                ScheduleSourceFull(
                    type = ScheduleSourceType.COMPLEX,
                    id = "{}",
                    title = "Расширенный поиск",
                    description = "",
                    avatar = null,
                ),
            )
            router.back()
        }
    }
}
