package io.edugma.features.schedule.sources

import androidx.lifecycle.viewModelScope
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import io.edugma.domain.schedule.model.source.ScheduleSourcesTabs
import io.edugma.domain.schedule.usecase.ScheduleSourcesUseCase
import io.edugma.features.base.core.mvi.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScheduleSourcesViewModel(
    private val useCase: ScheduleSourcesUseCase,
): BaseViewModel<ScheduleSourceState>(ScheduleSourceState()) {
    init {
        setupMutator {
            forProp { tabs }.onChanged {
                if (state.selectedTab !in state.tabs) {
                    state.tabs.firstOrNull()?.let {
                        state = state.copy(selectedTab = it)
                    }
                }
            }

            forProps { listOf(sources, query) }.onChanged {
                val filteredSources = state.sources
                    .filter {
                        state.query.isEmpty() ||
                                it.title.contains(state.query, ignoreCase = true)
                    }

                state = state.copy(filteredSources = filteredSources)
            }
        }

        viewModelScope.launch {
            useCase.getSourceTypes()
                .onSuccess { mutateState { state = state.copy(tabs = it) } }
                .onFailure { mutateState { state = state.copy(tabs = emptyList()) } }
                .collect()
        }

        viewModelScope.launch {
            state.map { it.selectedTab }
                .filterNotNull()
                .distinctUntilChanged()
                .collectLatest {
                    useCase.getScheduleSources(it)
                        .onSuccess { mutateState { state = state.copy(sources = it) } }
                        .onFailure { mutateState { state = state.copy(sources = emptyList()) } }
                        .collect()
            }
        }
    }

    fun onSelectTab(sourceType: ScheduleSourcesTabs) {
        mutateState {
            state = state.copy(selectedTab = sourceType)
        }
    }

    fun onSelectSource(source: ScheduleSourceFull) {
        viewModelScope.launch {
            useCase.setSelectedSource(source)
            router.exit()
        }
    }

    fun onAddFavorite(source: ScheduleSourceFull) {
        viewModelScope.launch {
            useCase.addFavoriteSource(source)
        }
    }

    fun onDeleteFavorite(source: ScheduleSourceFull) {
        viewModelScope.launch {
            useCase.deleteFavoriteSource(source)
        }
    }

    fun onQueryChange(query: String) {
        mutateState {
            state = state.copy(query = query)
        }
    }
}

data class ScheduleSourceState(
    val tabs: List<ScheduleSourcesTabs> = ScheduleSourcesTabs.values().toList(),
    val selectedTab: ScheduleSourcesTabs? = ScheduleSourcesTabs.Favorite,
    val query: String = "",
    val sources: List<ScheduleSourceFull> = emptyList(),
    val filteredSources: List<ScheduleSourceFull> = emptyList()
)