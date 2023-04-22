package io.edugma.features.account.marks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.nothingFound.EdNothingFound
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.domain.account.model.Performance
import io.edugma.features.account.marks.bottomSheets.FiltersBottomSheetContent
import io.edugma.features.account.marks.bottomSheets.PerformanceBottomSheetContent
import io.edugma.features.account.marks.item.FiltersRow
import io.edugma.features.account.marks.item.PerformanceItem
import io.edugma.features.account.marks.item.PerformancePlaceholder
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.core.utils.isNull
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PerformanceScreen(viewModel: PerformanceViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()
    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val scope = rememberCoroutineScope()

    FeatureScreen(navigationBarPadding = false) {
        ModalBottomSheetLayout(
            sheetState = bottomState,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            scrimColor = Color.Black.copy(alpha = 0.5f),
            sheetBackgroundColor = EdTheme.colorScheme.surface,
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
}

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
                if (!state.isLoading) {
                    IconButton(
                        onClick = { showBottomSheet(null) },
                    ) {
                        Icon(
                            painterResource(id = EdIcons.ic_fluent_filter_24_regular),
                            contentDescription = "Фильтр",
                        )
                    }
                }
            },
        )
        FiltersRow(state.currentFilters, filterClickListener)
        EdPullRefresh(refreshing = state.isRefreshing, onRefresh = retryListener) {
            when {
                state.isError && state.data.isNull() -> {
                    ErrorWithRetry(modifier = Modifier.fillMaxSize(), retryAction = retryListener)
                }
                state.filteredData?.isEmpty() == true -> {
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
                var showCourse = true
                if (it > 0) {
                    showCourse =
                        state.filteredData!![it].course != state.filteredData[it - 1].course
                }
                if (!showCourse && it > 0) {
                    Divider()
                    SpacerHeight(height = 3.dp)
                }
                if (showCourse) {
                    if (it > 0) SpacerHeight(height = 15.dp)
                    Text(
                        text = "${state.filteredData!![it].course} курс",
                        style = EdTheme.typography.headlineSmall,
                        color = EdTheme.colorScheme.primary,
                    )
                    SpacerHeight(height = 15.dp)
                }
                PerformanceItem(
                    state.filteredData!![it],
                ) { showBottomSheet(state.filteredData[it]) }
                SpacerHeight(height = 3.dp)
            }
        }
    }
}
