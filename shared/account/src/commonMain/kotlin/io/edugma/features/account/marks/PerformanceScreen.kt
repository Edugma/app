package io.edugma.features.account.marks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.bottomSheet.ModalBottomSheetValue
import io.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.nothingFound.EdNothingFound
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.utils.navigationBarsPadding
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.FeatureBottomSheetScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.isNull
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.account.domain.model.performance.Performance
import io.edugma.features.account.marks.bottomSheets.FiltersBottomSheetContent
import io.edugma.features.account.marks.bottomSheets.PerformanceBottomSheetContent
import io.edugma.features.account.marks.item.FiltersRow
import io.edugma.features.account.marks.item.PerformanceItem
import io.edugma.features.account.marks.item.PerformancePlaceholder
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun PerformanceScreen(viewModel: PerformanceViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()
    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val scope = rememberCoroutineScope()

    FeatureBottomSheetScreen(
        navigationBarPadding = false,
        sheetState = bottomState,
        sheetContent = {
            if (state.selectedPerformance.isNull()) {
                FiltersBottomSheetContent(
                    state = state,
                    filterUpdateListener = viewModel::updateFilter,
                    resetFilterListener = viewModel::resetFilters,
                )
            } else {
                PerformanceBottomSheetContent(performance = state.selectedPerformance!!)
            }
        },
    ) {
        PerformanceContent(
            state,
            showBottomSheet = {
                viewModel.openBottomSheetClick(it)
                scope.launch { bottomState.show() }
            },
            retryListener = viewModel::loadMarks,
            filterClickListener = viewModel::updateFilter,
            backListener = viewModel::exit,
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PerformanceContent(
    state: MarksState,
    showBottomSheet: Typed1Listener<Performance?>,
    retryListener: ClickListener,
    filterClickListener: Typed1Listener<Filter<*>>,
    backListener: ClickListener,
) {
    Column(Modifier.navigationBarsPadding()) {
        EdTopAppBar(
            title = "Оценки",
            onNavigationClick = backListener,
            actions = {
                IconButton(
                    onClick = { showBottomSheet(null) },
                    enabled = !state.isLoading,
                ) {
                    Icon(
                        painterResource(EdIcons.ic_fluent_filter_24_regular),
                        contentDescription = "Фильтр",
                    )
                }
            },
        )
        FiltersRow(state.currentFilters, filterClickListener)
        EdPullRefresh(refreshing = state.isRefreshing, onRefresh = retryListener) {
            when {
                state.showError -> {
                    ErrorWithRetry(modifier = Modifier.fillMaxSize(), retryAction = retryListener)
                }
                state.showNothingFound -> {
                    EdNothingFound(modifier = Modifier.fillMaxSize())
                }
                else -> PerformanceList(state, showBottomSheet)
            }
        }
    }
}

@Composable
fun PerformanceList(
    state: MarksState,
    showBottomSheet: Typed1Listener<Performance?>,
) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        if (state.placeholders) {
            items(3) {
                SpacerHeight(height = 3.dp)
                PerformancePlaceholder()
                Divider()
            }
        } else {
            items(
                count = state.filteredData?.size ?: 0,
                key = { state.filteredData!![it].id },
            ) {
                PerformanceItem(
                    state.filteredData!![it],
                ) { showBottomSheet(state.filteredData?.get(it)) }
                SpacerHeight(height = 3.dp)
            }
        }
    }
}
