package io.edugma.features.schedule.sources

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.arch.mvi.viewmodel.rememberOnAction
import io.edugma.core.arch.pagination.PaginationState
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.NavigationBarSpacer
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.chip.EdChipLabel
import io.edugma.core.designSystem.molecules.searchField.EdSearchField
import io.edugma.core.designSystem.organism.bottomSheet.ModalBottomSheetValue
import io.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import io.edugma.core.designSystem.organism.cell.EdCell
import io.edugma.core.designSystem.organism.cell.EdCellDefaults
import io.edugma.core.designSystem.organism.shortInfoSheet.EdShortInfoSheet
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.core.icons.EdIcons
import io.edugma.core.resources.MR
import io.edugma.core.ui.pagination.PagingFooter
import io.edugma.core.ui.screen.FeatureBottomSheetScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.schedule.domain.model.source.ScheduleSourceType
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun ScheduleSourcesScreen(viewModel: ScheduleSourcesViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()

    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(state.showBottomSheet) {
        if (state.showBottomSheet) {
            scope.launch { bottomState.show() }
        }
    }

    LaunchedEffect(bottomState) {
        snapshotFlow {
            bottomState.currentState
        }.collect {
            if (it == ModalBottomSheetValue.Hidden) {
                viewModel.onBottomSheetClosed()
            }
        }
    }

    FeatureBottomSheetScreen(
        statusBarPadding = false,
        navigationBarPadding = false,
        sheetState = bottomState,
        sheetContent = {
            ScheduleSourcesSheetContent(
                state = state,
                onAction = viewModel.rememberOnAction(),
            )
        },
    ) {
        ScheduleSourcesContent(
            state = state,
            onBackClick = viewModel::exit,
            onQueryChange = viewModel::onQueryChange,
            onTabSelected = viewModel::onSelectTab,
            onApplyComplexSearch = viewModel::onApplyComplexSearch,
            onAction = viewModel.rememberOnAction(),
        )
    }
}

@Composable
fun ScheduleSourcesSheetContent(
    state: ScheduleSourceState,
    onAction: (ScheduleSourcesAction) -> Unit,
) {
    if (state.selectedSource != null) {
        EdShortInfoSheet(
            title = state.selectedSource.source.title,
            avatar = state.selectedSource.source.avatar,
            description = state.selectedSource.source.description.orEmpty(),
        ) {
            EdButton(
                text = "Выбрать",
                onClick = {
                    onAction(ScheduleSourcesAction.OnSourceSelected(state.selectedSource))
                },
                modifier = Modifier.fillMaxWidth(),
            )
            EdButton(
                text = "Добавить в избранное",
                onClick = {
                    onAction(ScheduleSourcesAction.OnAddToFavorite(state.selectedSource))
                },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ScheduleSourcesContent(
    state: ScheduleSourceState,
    onBackClick: ClickListener,
    onQueryChange: Typed1Listener<String>,
    onTabSelected: Typed1Listener<ScheduleSourceType>,
    onApplyComplexSearch: ClickListener,
    onAction: (ScheduleSourcesAction) -> Unit,
) {

    val q = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    Column {
        EdSurface(
            elevation = EdElevation.Level1,
            shape = EdTheme.shapes.large,
        ) {
            Column(Modifier.fillMaxWidth()) {
                EdTopAppBar(
                    title = stringResource(MR.strings.schedule_sou_schedule_selection),
                    onNavigationClick = onBackClick,
                    windowInsets = WindowInsets.statusBars,
                )
                SourceTypeTabs(
                    tabs = state.tabs,
                    selectedTab = state.selectedTab,
                    onTabSelected = onTabSelected,
                )
                EdSearchField(
                    value = state.query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .padding(horizontal = EdTheme.paddings.s)
                        .fillMaxWidth(),
                    placeholder = stringResource(MR.strings.schedule_sou_search),
                )
                SpacerHeight(height = EdTheme.paddings.s)
            }
        }
        if (state.selectedTab?.id == ScheduleSourceType.COMPLEX) {
            ComplexSearch(
                typesId = emptyList(),
                subjectsId = emptyList(),
                teachersId = emptyList(),
                groupsId = emptyList(),
                placesId = emptyList(),
                onSelectTypes = { },
                onSelectSubjects = { },
                onSelectTeachers = { },
                onSelectGroups = { },
                onSelectPlaces = { },
                onApply = onApplyComplexSearch,
            )
        } else {
            Search(
                paging = state.paginationState,
                onSourceClick = {
                    onAction(ScheduleSourcesAction.OnSourceClicked(it))
                },
                onAddFavorite = {
                    onAction(ScheduleSourcesAction.OnAddToFavorite(it))
                },
                onDeleteFavorite = {
                    onAction(ScheduleSourcesAction.OnDeleteFromFavorite(it))
                },
                onLoadPage = {
                    onAction(ScheduleSourcesAction.OnLoadPage)
                },
            )
        }
    }

//    (
//        sheetContent = {
// //            FiltersSelector(
// //                filters = TODO(),
// //                query = TODO(),
// //                onFilterSelected = TODO(),
// //                onQueryChange = TODO()
// //            )
//        },
//        sheetState = q,
//    ) {
//        Column {
//            PrimaryTopAppBar(
//                title = stringResource(R.string.schedule_sou_schedule_selection),
//                onBackClick = onBackClick
//            )
//            SourceTypeTabs(
//                tabs = state.tabs,
//                selectedTab = state.selectedTab,
//                onTabSelected = onTabSelected
//            )
//            if (state.selectedTab == ScheduleSourcesTabs.Complex) {
//                ComplexSearch(
//                    typesId = TODO(),
//                    subjectsId = TODO(),
//                    teachersId = TODO(),
//                    groupsId = TODO(),
//                    placesId = TODO(),
//                    onSelectTypes = TODO(),
//                    onSelectSubjects = TODO(),
//                    onSelectTeachers = TODO(),
//                    onSelectGroups = TODO(),
//                    onSelectPlaces = TODO()
//                )
//            } else {
//                Search(
//                    query = state.query,
//                    filteredSources = state.filteredSources,
//                    selectedTab = state.selectedTab,
//                    onQueryChange = onQueryChange,
//                    onSourceSelected = onSourceSelected,
//                    onAddFavorite = onAddFavorite,
//                    onDeleteFavorite = onDeleteFavorite
//                )
//            }
//        }
//    }
}

@Composable
private fun FiltersSelector(
    filters: List<Pair<String, Boolean>>,
    query: String,
    onFilterSelected: Typed1Listener<String>,
    onQueryChange: Typed1Listener<String>,
) {
    Column(Modifier.fillMaxSize()) {
        EdSearchField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            placeholder = stringResource(MR.strings.schedule_sou_search),
        )
        LazyColumn(Modifier.fillMaxWidth()) {
            items(filters) {
                val tonalElevation = remember(it.second) {
                    if (it.second) EdElevation.Level3 else EdElevation.Level1
                }
                EdCard(
                    onClick = { onFilterSelected(it.first) },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = tonalElevation,
                ) {
                    EdLabel(text = it.first)
                }
            }
        }
    }
}

@Composable
private fun SourceTypeTabs(
    tabs: List<ScheduleSourceType>,
    selectedTab: ScheduleSourceType?,
    onTabSelected: Typed1Listener<ScheduleSourceType>,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = EdTheme.paddings.xxs),
    ) {
        items(tabs) { tab ->
            EdChipLabel(
                text = tab.title,
                selected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
            )
        }
    }
}

@Composable
private fun ComplexSearch(
    typesId: List<String>,
    subjectsId: List<String>,
    teachersId: List<String>,
    groupsId: List<String>,
    placesId: List<String>,
    onSelectTypes: ClickListener,
    onSelectSubjects: ClickListener,
    onSelectTeachers: ClickListener,
    onSelectGroups: ClickListener,
    onSelectPlaces: ClickListener,
    onApply: ClickListener,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Filter(
            title = "Типы занятий",
            filters = typesId,
            onSelect = onSelectTypes,
        )
        SpacerHeight(height = 16.dp)
        Filter(
            title = "Предметы",
            filters = subjectsId,
            onSelect = onSelectSubjects,
        )
        SpacerHeight(height = 16.dp)
        Filter(
            title = "Преподаватели",
            filters = teachersId,
            onSelect = onSelectTeachers,
        )
        SpacerHeight(height = 16.dp)
        Filter(
            title = "Группы",
            filters = groupsId,
            onSelect = onSelectGroups,
        )
        SpacerHeight(height = 16.dp)
        Filter(
            title = "Места",
            filters = placesId,
            onSelect = onSelectPlaces,
        )
        SpacerHeight(height = 16.dp)
        EdButton(
            onClick = onApply,
            modifier = Modifier.fillMaxWidth(),
            text = "Применить",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun Filter(
    title: String,
    filters: List<String>,
    onSelect: ClickListener,
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            EdLabel(text = title)
            EdLabel(
                text = "Посмотреть все",
                iconPainter = painterResource(EdIcons.ic_fluent_ios_arrow_rtl_24_regular),
                modifier = Modifier.clickable(onClick = onSelect),
                iconStart = false,
            )
        }
        LazyRow {
            items(filters) {
                InputChip(
                    onClick = { },
                    label = {
                        EdLabel(text = it)
                    },
                    selected = false,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColumnScope.Search(
    paging: PaginationState<ScheduleSourceUiModel>,
    onSourceClick: (ScheduleSourceUiModel) -> Unit,
    onAddFavorite: (ScheduleSourceUiModel) -> Unit,
    onDeleteFavorite: (ScheduleSourceUiModel) -> Unit,
    onLoadPage: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 10.dp),
    ) {
        items(
            items = paging.items,
            key = { it.source.id to it.source.type },
            contentType = { "source" },
        ) { source ->
            SourceItem(
                source = source,
                onItemClick = onSourceClick,
                onAddFavorite = onAddFavorite,
                onDeleteFavorite = onDeleteFavorite,
                modifier = Modifier.animateItemPlacement(),
            )
        }
        item { PagingFooter(paging, onLoadPage) }
        item {
            NavigationBarSpacer(10.dp)
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SourceItem(
    source: ScheduleSourceUiModel,
    onItemClick: (ScheduleSourceUiModel) -> Unit,
    onAddFavorite: (ScheduleSourceUiModel) -> Unit,
    onDeleteFavorite: (ScheduleSourceUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    EdCell(
        title = source.source.title,
        subtitle = source.source.description.orEmpty(),
        avatar = source.source.avatar,
        onClick = {
            onItemClick(source)
        },
        contentPadding = EdCellDefaults.contentPadding(end = 4.dp),
        modifier = modifier,
    ) {
        IconButton(
            onClick = {
                if (source.isFavorite) {
                    onDeleteFavorite(source)
                } else {
                    onAddFavorite(source)
                }
            },
        ) {
            val tintColor = if (source.isFavorite) {
                EdTheme.colorScheme.primary
            } else {
                LocalContentColor.current
            }
            val painter = if (source.isFavorite) {
                painterResource(EdIcons.ic_fluent_star_24_filled)
            } else {
                painterResource(EdIcons.ic_fluent_star_24_regular)
            }
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painter,
                contentDescription = null,
                tint = tintColor,
            )
        }
    }
}
