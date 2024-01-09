package io.edugma.features.account.performance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.arch.mvi.viewmodel.rememberOnAction
import io.edugma.core.designSystem.atoms.spacer.NavigationBarSpacer
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.EdScaffold
import io.edugma.core.designSystem.organism.bottomSheet.ModalBottomSheetValue
import io.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import io.edugma.core.designSystem.organism.chipRow.EdChipLabelLazyRow
import io.edugma.core.designSystem.organism.lceScaffold.EdLceScaffold
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.FeatureBottomSheetScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.isNull
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.account.domain.model.performance.GradePosition
import io.edugma.features.account.performance.bottomSheets.FiltersBottomSheetContent
import io.edugma.features.account.performance.bottomSheets.PerformanceBottomSheetContent
import io.edugma.features.account.performance.model.FiltersRow
import io.edugma.features.account.performance.model.PerformanceItem
import io.edugma.features.account.performance.model.PerformancePlaceholder
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun PerformanceScreen(viewModel: PerformanceViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()
    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.selectedPerformance) {
        if (state.selectedPerformance != null) {
            scope.launch { bottomState.show() }
        }
    }

    FeatureBottomSheetScreen(
        navigationBarPadding = false,
        statusBarPadding = false,
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
            filterClickListener = viewModel::updateFilter,
            backListener = viewModel::exit,
            onAction = viewModel.rememberOnAction(),
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PerformanceContent(
    state: PerformanceUiState,
    showBottomSheet: Typed1Listener<GradePosition?>,
    filterClickListener: Typed1Listener<Filter<*>>,
    backListener: ClickListener,
    onAction: (PerformanceAction) -> Unit,
) {
    EdScaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            Column(Modifier.fillMaxWidth()) {
                EdTopAppBar(
                    title = "Оценки",
                    onNavigationClick = backListener,
                    windowInsets = WindowInsets.statusBars,
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
                if (state.periods != null) {
                    EdChipLabelLazyRow(
                        items = state.periods,
                        selectedItem = state.selectedPeriod,
                        title = { it.value },
                        modifier = Modifier.padding(bottom = 10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp),
                    ) { selectedItem ->
                        onAction(PerformanceAction.OnPeriodSelected(selectedItem))
                    }
                }
                FiltersRow(state.currentFilters, filterClickListener)
            }
        },
    ) {
        EdLceScaffold(
            lceState = state.lceState,
            onRefresh = { onAction(PerformanceAction.OnRefresh) },
            placeholder = { PerformanceListPlaceholder() },
        ) {
            PerformanceList(
                state = state,
                onItemClick = {
                    onAction(PerformanceAction.OnPerformanceClicked(it))
                },
            )
        }
    }
}

@Composable
fun PerformanceList(
    state: PerformanceUiState,
    onItemClick: (GradePosition?) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        items(
            count = state.filteredData?.size ?: 0,
            key = { state.filteredData!![it].id },
        ) {
            PerformanceItem(
                performance = state.filteredData!![it],
                onClick = { onItemClick(state.filteredData?.get(it)) },
                modifier = Modifier.padding(bottom = 3.dp),
            )
        }

        item {
            NavigationBarSpacer()
        }
    }
}

@Composable
fun PerformanceListPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        repeat(6) {
            SpacerHeight(height = 3.dp)
            PerformancePlaceholder()
        }
    }
}
