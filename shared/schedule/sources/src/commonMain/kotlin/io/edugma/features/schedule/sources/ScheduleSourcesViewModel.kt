package io.edugma.features.schedule.sources

import androidx.compose.runtime.Immutable
import io.edugma.core.api.model.PagingDto
import io.edugma.core.api.model.map
import io.edugma.core.arch.mvi.delegate.debounce
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.arch.pagination.PaginationState
import io.edugma.core.arch.pagination.PagingViewModel
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSourceType
import io.edugma.features.schedule.domain.usecase.ScheduleSourcesUseCase
import kotlinx.coroutines.flow.map

class ScheduleSourcesViewModel(
    private val useCase: ScheduleSourcesUseCase,
    private val pagingViewModel: PagingViewModel<ScheduleSourceUiModel>,
) : BaseActionViewModel<ScheduleSourceState, ScheduleSourcesAction>(ScheduleSourceState()) {
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
                    ScheduleSourceUiModel(
                        source = it,
                        isFavorite = it in favoriteSources,
                    )
                }
            }
        }

        launchCoroutine(
            onError = {
                newState { setTabs(tabs = emptyList()) }
            },
        ) {
            val tabs = useCase.getSourceTypes()
            newState { setTabs(tabs = tabs) }
        }
    }

    private val queryDebounce = debounce()

    override fun onAction(action: ScheduleSourcesAction) {
        when (action) {
            ScheduleSourcesAction.OnLoadPage -> {
                pagingViewModel.loadNextPage()
            }

            is ScheduleSourcesAction.OnAddToFavorite -> onAddFavorite(action.source.source)
            is ScheduleSourcesAction.OnSourceSelected -> onSelectSource(action.source.source)
            is ScheduleSourcesAction.OnDeleteFromFavorite -> onDeleteFavorite(action.source.source)
            is ScheduleSourcesAction.OnSourceClicked -> onSourceClick(action.source)
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

    fun onSelectTab(selectedTab: ScheduleSourceType) {
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

    private fun onSelectSource(source: ScheduleSourceFull) {
        launchCoroutine {
            useCase.setSelectedSource(source)
            router.back()
        }
    }

    private fun onAddFavorite(source: ScheduleSourceFull) {
        launchCoroutine {
            useCase.addFavoriteSource(source)
        }
        pagingViewModel.resetAndLoad()
    }

    private fun onDeleteFavorite(source: ScheduleSourceFull) {
        launchCoroutine {
            useCase.deleteFavoriteSource(source)
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

@Immutable
data class ScheduleSourceState(
    val tabs: List<ScheduleSourceType> = emptyList(),
    val selectedTab: ScheduleSourceType? = null,
    val query: String = "",
    val filteredSources: List<ScheduleSourceUiModel> = emptyList(),
    val paginationState: PaginationState<ScheduleSourceUiModel> = PaginationState.empty(),
    val selectedSource: ScheduleSourceUiModel? = null,
    val showBottomSheet: Boolean = false,
) {
    fun setTabs(tabs: List<ScheduleSourceType>) =
        copy(tabs = tabs).updateSelectedTab()

    fun setQuery(query: String) =
        copy(query = query)

    fun updateSelectedTab(): ScheduleSourceState {
        return if (selectedTab !in tabs) {
            tabs.firstOrNull()?.let {
                copy(selectedTab = it)
            } ?: this
        } else {
            this
        }
    }
}

@Immutable
data class ScheduleSourceUiModel(
    val isFavorite: Boolean,
    val source: ScheduleSourceFull,
)
