package com.edugma.features.account.performance

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
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import com.edugma.core.arch.mvi.viewmodel.rememberOnAction
import com.edugma.core.designSystem.atoms.spacer.NavigationBarSpacer
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.organism.EdScaffold
import com.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import com.edugma.core.designSystem.organism.chipRow.EdChipLabelLazyRow
import com.edugma.core.designSystem.organism.lceScaffold.EdLceScaffold
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.shapes.top
import com.edugma.core.icons.EdIcons
import com.edugma.core.ui.screen.FeatureBottomSheetScreen
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.ClickListener
import com.edugma.core.utils.Typed1Listener
import com.edugma.core.utils.isNull
import com.edugma.core.utils.viewmodel.getViewModel
import com.edugma.features.account.domain.model.performance.GradePosition
import com.edugma.features.account.performance.bottomSheets.FiltersBottomSheetContent
import com.edugma.features.account.performance.bottomSheets.PerformanceBottomSheetContent
import com.edugma.features.account.performance.model.FiltersRow
import com.edugma.features.account.performance.model.PerformanceItem
import com.edugma.features.account.performance.model.PerformancePlaceholder
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun PerformanceScreen(viewModel: PerformanceViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()
    val bottomState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.selectedPerformance) {
        if (state.selectedPerformance != null) {
            scope.launch { bottomState.show() }
        }
    }

    FeatureScreen(
        navigationBarPadding = false,
        statusBarPadding = false,
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

    FeatureBottomSheetScreen(
        sheetState = bottomState,
    ) {
        if (state.selectedPerformance.isNull()) {
            FiltersBottomSheetContent(
                state = state,
                filterUpdateListener = viewModel::updateFilter,
                resetFilterListener = viewModel::resetFilters,
            )
        } else {
            PerformanceBottomSheetContent(performance = state.selectedPerformance!!)
        }
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
        EdSurface(
            shape = EdTheme.shapes.large.top(),
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
