package io.edugma.features.schedule.sources

import androidx.compose.runtime.Immutable
import io.edugma.core.arch.pagination.PaginationUiState
import io.edugma.core.arch.pagination.map
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSourceType
import io.edugma.features.schedule.sources.model.ScheduleSourceUiModel
import io.edugma.features.schedule.sources.model.toUiModel

@Immutable
data class ScheduleSourceUiState(
    val tabs: List<ScheduleSourceType> = emptyList(),
    val selectedTab: ScheduleSourceType? = null,
    val query: String = "",
    val filteredSources: List<ScheduleSourceUiModel> = emptyList(),
    val favoriteSources: List<ScheduleSourceFull> = emptyList(),
    val paginationState: PaginationUiState<ScheduleSourceFull> = PaginationUiState.empty(),
    val paginationUiState: PaginationUiState<ScheduleSourceUiModel> = PaginationUiState.empty(),
    val selectedSource: ScheduleSourceUiModel? = null,
    val showBottomSheet: Boolean = false,
) {
    fun setPagination(paginationState: PaginationUiState<ScheduleSourceFull>) =
        copy(paginationState = paginationState)

    fun setFavoriteSources(favoriteSources: List<ScheduleSourceFull>) =
        copy(favoriteSources = favoriteSources)

    // TODO проверить при быстрых лайках
    fun updatePaginationUi() = copy(
        paginationUiState = paginationState.map {
            it.toUiModel(it in this.favoriteSources)
        }
    )

    fun updateSelectedSource() = copy(
        selectedSource = selectedSource?.let { selectedSource ->
            selectedSource.copy(
                isFavorite = this.favoriteSources.any { it.id == selectedSource.id }
            )
        }
    )

    fun setTabs(tabs: List<ScheduleSourceType>) =
        copy(tabs = tabs).updateSelectedTab()

    fun setQuery(query: String) =
        copy(query = query)

    private fun updateSelectedTab(): ScheduleSourceUiState {
        return if (selectedTab !in tabs) {
            tabs.firstOrNull()?.let {
                copy(selectedTab = it)
            } ?: this
        } else {
            this
        }
    }
}
