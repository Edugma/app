package io.edugma.features.schedule.sources

import io.edugma.core.api.utils.onFailure
import io.edugma.core.api.utils.onSuccess
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
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
        launchCoroutine {
            useCase.getSourceTypes()
                .onSuccess { newState { setTabs(tabs = it) } }
                .onFailure { newState { setTabs(tabs = emptyList()) } }
                .collect()
        }

        launchCoroutine(
            onError = {
                newState {
                    copy(sources = emptyList())
                }
            },
        ) {
            stateFlow.map { it.selectedTab }
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
                        newState { setSources(sources = uiModel) }
                    }
                }.catch {
                    newState { setSources(sources = emptyList()) }
                }.collect()
        }
    }

    fun onSelectTab(sourceType: ScheduleSourcesTabs) {
        newState {
            copy(selectedTab = sourceType)
        }
    }

    fun onSelectSource(source: ScheduleSourceFull) {
        launchCoroutine {
            useCase.setSelectedSource(source)
            router.back()
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
        newState {
            setQuery(query = query)
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
            router.back()
        }
    }
}

data class ScheduleSourceState(
    val tabs: List<ScheduleSourcesTabs> = ScheduleSourcesTabs.getAllTabs(),
    val selectedTab: ScheduleSourcesTabs? = ScheduleSourcesTabs.Favorite,
    val query: String = "",
    val sources: List<ScheduleSourceUiModel> = emptyList(),
    val filteredSources: List<ScheduleSourceUiModel> = emptyList(),
) {
    fun setTabs(tabs: List<ScheduleSourcesTabs>) =
        copy(tabs = tabs).updateSelectedTab()

    fun setQuery(query: String) =
        copy(query = query)
            .updateFilteredSources()

    fun setSources(sources: List<ScheduleSourceUiModel>) =
        copy(sources = sources)
            .updateFilteredSources()

    fun updateFilteredSources(): ScheduleSourceState {
        val filteredSources = sources
            .filter {
                query.isEmpty() ||
                    it.source.title.contains(query, ignoreCase = true)
            }

        return copy(filteredSources = filteredSources)
    }

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

data class ScheduleSourceUiModel(
    val isFavorite: Boolean,
    val source: ScheduleSourceFull,
)

data class ScheduleFilterState(
    val filters: List<Pair<String, Boolean>> = emptyList(),
    val query: String = "",
)
