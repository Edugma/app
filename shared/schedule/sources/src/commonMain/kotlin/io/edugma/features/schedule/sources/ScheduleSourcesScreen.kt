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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp


import edugma.shared.core.resources.generated.resources.Res
import edugma.shared.core.resources.generated.resources.*
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.edugma.core.arch.mvi.viewmodel.rememberOnAction
import io.edugma.core.arch.pagination.PaginationUiState
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.NavigationBarSpacer
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.chip.EdChipLabel
import io.edugma.core.designSystem.molecules.iconButton.EdIconButton
import io.edugma.core.designSystem.molecules.searchField.EdSearchField
import io.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import io.edugma.core.designSystem.organism.cell.EdCell
import io.edugma.core.designSystem.organism.cell.EdCellDefaults
import io.edugma.core.designSystem.organism.cell.EdCellPlaceholder
import io.edugma.core.designSystem.organism.lceScaffold.EdLceScaffold
import io.edugma.core.designSystem.organism.shortInfoSheet.EdShortInfoSheet
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.pagination.PagingFooter
import io.edugma.core.ui.screen.FeatureBottomSheetScreen
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.schedule.domain.model.source.ScheduleSourceType
import io.edugma.features.schedule.sources.model.ScheduleSourceUiModel
import kotlinx.coroutines.launch

@Composable
fun ScheduleSourcesScreen(viewModel: ScheduleSourcesViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()

    val bottomState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()

    LaunchedEffect(state.showBottomSheet) {
        if (state.showBottomSheet) {
            scope.launch { bottomState.show() }
            viewModel.onBottomSheetClosed()
        }
    }

    val filledFavoritePainter = painterResource(EdIcons.ic_fluent_star_24_filled)
    val regularFavoritePainter = painterResource(EdIcons.ic_fluent_star_24_regular)

    val favoriteListPainterProvider: (Boolean) -> Painter = remember {
        { isFavorite: Boolean ->
            if (isFavorite) {
                filledFavoritePainter
            } else {
                regularFavoritePainter
            }
        }
    }

    FeatureScreen(
        statusBarPadding = false,
        navigationBarPadding = false,
    ) {
        ScheduleSourcesContent(
            state = state,
            favoritePainterProvider = favoriteListPainterProvider,
            onBackClick = viewModel::exit,
            onQueryChange = viewModel::onQueryChange,
            onApplyComplexSearch = viewModel::onApplyComplexSearch,
            onAction = viewModel.rememberOnAction(),
        )
    }

    FeatureBottomSheetScreen(
        statusBarPadding = false,
        navigationBarPadding = false,
        sheetState = bottomState,
    ) {
        ScheduleSourcesSheetContent(
            state = state,
            onAction = viewModel.rememberOnAction(),
        )
    }
}

@Composable
fun ScheduleSourcesSheetContent(
    state: ScheduleSourceUiState,
    onAction: (ScheduleSourcesAction) -> Unit,
) {
    if (state.selectedSource != null) {
        EdShortInfoSheet(
            title = state.selectedSource.title,
            avatar = state.selectedSource.avatar,
            description = state.selectedSource.description.orEmpty(),
        ) {
            Row(Modifier.fillMaxWidth()) {
                EdButton(
                    text = "Выбрать",
                    onClick = {
                        onAction(ScheduleSourcesAction.OnSourceSelected(state.selectedSource))
                    },
                    modifier = Modifier.weight(1f),
                )
                SpacerWidth(16.dp)

                val painter = if (state.selectedSource.isFavorite) {
                    painterResource(EdIcons.ic_fluent_star_24_filled)
                } else {
                    painterResource(EdIcons.ic_fluent_star_24_regular)
                }

                EdIconButton(
                    painter = painter,
                    onClick = {
                        if (state.selectedSource.isFavorite) {
                            onAction(
                                ScheduleSourcesAction.OnDeleteFromFavorite(state.selectedSource),
                            )
                        } else {
                            onAction(ScheduleSourcesAction.OnAddToFavorite(state.selectedSource))
                        }
                    },
                    modifier = Modifier,
                )
            }

        }
    }
}

@Composable
private fun ScheduleSourcesContent(
    state: ScheduleSourceUiState,
    favoritePainterProvider: (Boolean) -> Painter,
    onBackClick: ClickListener,
    onQueryChange: Typed1Listener<String>,
    onApplyComplexSearch: ClickListener,
    onAction: (ScheduleSourcesAction) -> Unit,
) {

    val q = rememberModalBottomSheetState()

    Column {
        EdSurface(
            elevation = EdElevation.Level1,
            shape = EdTheme.shapes.large,
        ) {
            Column(Modifier.fillMaxWidth()) {
                EdTopAppBar(
                    title = stringResource(Res.string.schedule_sou_schedule_selection),
                    onNavigationClick = onBackClick,
                    windowInsets = WindowInsets.statusBars,
                )
                SourceTypeTabs(
                    tabs = state.tabs,
                    selectedTab = state.selectedTab,
                    onTabSelected = {
                        onAction(ScheduleSourcesAction.OnTabSelected(it))
                    },
                )
                EdSearchField(
                    value = state.query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .padding(horizontal = EdTheme.paddings.s)
                        .fillMaxWidth(),
                    placeholder = stringResource(Res.string.schedule_sou_search),
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
            EdLceScaffold(
                lceState = state.paginationUiState.lceState,
                onRefresh = { onAction(ScheduleSourcesAction.OnRefresh) },
                placeholder = { ScheduleSourceListPlaceholder() },
            ) {
                ScheduleSourceList(
                    paging = state.paginationUiState,
                    favoritePainterProvider = favoritePainterProvider,
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
    }
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
            placeholder = stringResource(Res.string.schedule_sou_search),
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

@OptIn(ExperimentalMaterial3Api::class)
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
private fun ColumnScope.ScheduleSourceList(
    paging: PaginationUiState<ScheduleSourceUiModel>,
    favoritePainterProvider: (Boolean) -> Painter,
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
            key = { it.listKey },
            contentType = { it.listContentType },
        ) { source ->
            SourceItem(
                source = source,
                favoritePainterProvider = favoritePainterProvider,
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

@Composable
fun SourceItem(
    source: ScheduleSourceUiModel,
    favoritePainterProvider: (Boolean) -> Painter,
    onItemClick: (ScheduleSourceUiModel) -> Unit,
    onAddFavorite: (ScheduleSourceUiModel) -> Unit,
    onDeleteFavorite: (ScheduleSourceUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    EdCell(
        title = source.title,
        subtitle = source.description.orEmpty(),
        avatar = source.avatar,
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
            val painter = favoritePainterProvider(source.isFavorite)
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painter,
                contentDescription = null,
                tint = tintColor,
            )
        }
    }
}

@Composable
private fun ScheduleSourceListPlaceholder() {
    Column(modifier = Modifier.fillMaxSize()) {
        repeat(6) {
            ScheduleSourceItemPlaceholder()
        }
    }
}

@Composable
fun ScheduleSourceItemPlaceholder() {
    EdCellPlaceholder(modifier = Modifier.fillMaxWidth())
}
