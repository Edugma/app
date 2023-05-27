package io.edugma.features.schedule.sources

import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import io.edugma.features.schedule.domain.model.source.ScheduleSourcesTabs
import io.edugma.features.schedule.domain.usecase.ScheduleSourcesUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class ScheduleSourcesViewModel(
    private val useCase: ScheduleSourcesUseCase,
) : BaseViewModel<ScheduleSourceState>(ScheduleSourceState()) {
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
                            it.source.title.contains(state.query, ignoreCase = true)
                    }

                state = state.copy(filteredSources = filteredSources)
            }
        }

        launchCoroutine {
            useCase.getSourceTypes()
                .onSuccess { mutateState { state = state.copy(tabs = it) } }
                .onFailure { mutateState { state = state.copy(tabs = emptyList()) } }
                .collect()
        }

        launchCoroutine(
            onError = {
                mutateState { state = state.copy(sources = emptyList()) }
            },
        ) {
            state.map { it.selectedTab }
                .filterNotNull()
                .filter { it != ScheduleSourcesTabs.Complex }
                .distinctUntilChanged()
                .flatMapLatest { tab ->
                    val sources = useCase.getScheduleSources(tab)
                    val favoriteSources = useCase.getFavoriteSources()
                    combine(sources, favoriteSources) { sources, favoriteSources ->
                        val uiModel = sources.map {
                            ScheduleSourceUiModel(
                                source = it,
                                isFavorite = it in favoriteSources,
                            )
                        }
                        mutateState { state = state.copy(sources = uiModel) }
                    }
                }.catch {
                    mutateState { state = state.copy(sources = emptyList()) }
                }.collect()
        }
    }

    fun onSelectTab(sourceType: ScheduleSourcesTabs) {
        mutateState {
            state = state.copy(selectedTab = sourceType)
        }
    }

    fun onSelectSource(source: ScheduleSourceFull) {
        launchCoroutine {
            useCase.setSelectedSource(source)
            router.exit()
        }
    }

    fun onAddFavorite(source: ScheduleSourceFull) {
        launchCoroutine {
            useCase.addFavoriteSource(source)
        }
    }

    fun onDeleteFavorite(source: ScheduleSourceFull) {
        launchCoroutine {
            useCase.deleteFavoriteSource(source)
        }
    }

    fun onQueryChange(query: String) {
        mutateState {
            state = state.copy(query = query)
        }
    }

    fun onApplyComplexSearch() {
        launchCoroutine {
            useCase.setSelectedSource(
                ScheduleSourceFull(
                    type = ScheduleSources.Complex,
                    key = "{}",
                    title = "Расширенный поиск",
                    description = "",
                    avatarUrl = null,
                ),
            )
            router.exit()
        }
    }
}

data class ScheduleSourceState(
    val tabs: List<ScheduleSourcesTabs> = ScheduleSourcesTabs.values().toList(),
    val selectedTab: ScheduleSourcesTabs? = ScheduleSourcesTabs.Favorite,
    val query: String = "",
    val sources: List<ScheduleSourceUiModel> = emptyList(),
    val filteredSources: List<ScheduleSourceUiModel> = emptyList(),
)

data class ScheduleSourceUiModel(
    val isFavorite: Boolean,
    val source: ScheduleSourceFull,
)

data class ScheduleFilterState(
    val filters: List<Pair<String, Boolean>> = emptyList(),
    val query: String = "",
)
