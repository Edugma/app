package io.edugma.features.schedule.sources

import androidx.compose.runtime.Immutable
import io.edugma.core.arch.pagination.PaginationUiState
import io.edugma.features.schedule.domain.model.source.ScheduleSourceType
import io.edugma.features.schedule.sources.model.ScheduleSourceUiModel

@Immutable
data class ScheduleSourceUiState(
    val tabs: List<ScheduleSourceType> = emptyList(),
    val selectedTab: ScheduleSourceType? = null,
    val query: String = "",
    val filteredSources: List<ScheduleSourceUiModel> = emptyList(),
    val paginationState: PaginationUiState<ScheduleSourceUiModel> = PaginationUiState.empty(),
    val selectedSource: ScheduleSourceUiModel? = null,
    val showBottomSheet: Boolean = false,
) {
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
