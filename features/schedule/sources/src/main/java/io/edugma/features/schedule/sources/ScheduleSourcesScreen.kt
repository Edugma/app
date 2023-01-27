package io.edugma.features.schedule.sources

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.searchField.EdSearchField
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import io.edugma.domain.schedule.model.source.ScheduleSources
import io.edugma.domain.schedule.model.source.ScheduleSourcesTabs
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.*
import org.koin.androidx.compose.getViewModel

@Composable
fun ScheduleSourcesScreen(viewModel: ScheduleSourcesViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    ScheduleSourcesContent(
        state = state,
        onBackClick = viewModel::exit,
        onQueryChange = viewModel::onQueryChange,
        onTabSelected = viewModel::onSelectTab,
        onSourceSelected = viewModel::onSelectSource,
        onAddFavorite = viewModel::onAddFavorite,
        onDeleteFavorite = viewModel::onDeleteFavorite,
        onApplyComplexSearch = viewModel::onApplyComplexSearch,
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScheduleSourcesContent(
    state: ScheduleSourceState,
    onBackClick: ClickListener,
    onQueryChange: Typed1Listener<String>,
    onTabSelected: Typed1Listener<ScheduleSourcesTabs>,
    onSourceSelected: Typed1Listener<ScheduleSourceFull>,
    onAddFavorite: Typed1Listener<ScheduleSourceFull>,
    onDeleteFavorite: Typed1Listener<ScheduleSourceFull>,
    onApplyComplexSearch: ClickListener,
) {

    val q = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    Column {
        EdTopAppBar(
            title = stringResource(R.string.schedule_sou_schedule_selection),
            onNavigationClick = onBackClick,
        )
        SourceTypeTabs(
            tabs = state.tabs,
            selectedTab = state.selectedTab,
            onTabSelected = onTabSelected,
        )
        if (state.selectedTab == ScheduleSourcesTabs.Complex) {
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
                query = state.query,
                filteredSources = state.filteredSources,
                selectedTab = state.selectedTab,
                onQueryChange = onQueryChange,
                onSourceSelected = onSourceSelected,
                onAddFavorite = onAddFavorite,
                onDeleteFavorite = onDeleteFavorite,
            )
        }
    }

//    ModalBottomSheetLayout(
//        sheetContent = {
// //            FiltersSelector(
// //                filters = TODO(),
// //                query = TODO(),
// //                onFilterSelected = TODO(),
// //                onQueryChange = TODO()
// //            )
//        },
//        sheetState = q,
//        sheetBackgroundColor = EdTheme.colorScheme.background
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

@OptIn(ExperimentalMaterial3Api::class)
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
            placeholder = stringResource(R.string.schedule_sou_search),
        )
        LazyColumn(Modifier.fillMaxWidth()) {
            items(filters) {
                val tonalElevation = remember(it.second) {
                    if (it.second) 4.dp else 0.dp
                }
                EdCard(
                    onClick = { onFilterSelected(it.first) },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = tonalElevation),
                ) {
                    Text(text = it.first)
                }
            }
        }
    }
}

@Composable
private fun SourceTypeTabs(
    tabs: List<ScheduleSourcesTabs>,
    selectedTab: ScheduleSourcesTabs?,
    onTabSelected: Typed1Listener<ScheduleSourcesTabs>,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
    ) {
        item {
            SpacerWidth(10.dp)
        }
        items(tabs) { tab ->
            SourceTypeTab(
                tab,
                tab == selectedTab,
                onTabSelected = onTabSelected,
            )
        }
        item {
            SpacerWidth(10.dp)
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
            Text(text = title)
            EdLabel(
                text = "Посмотреть все",
                iconPainter = painterResource(FluentIcons.ic_fluent_ios_arrow_rtl_24_regular),
                modifier = Modifier.clickable(onClick = onSelect),
                iconStart = false,
            )
        }
        LazyRow {
            items(filters) {
                InputChip(
                    onClick = { },
                    label = {
                        Text(text = it)
                    },
                    selected = false,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun Search(
    query: String,
    filteredSources: List<ScheduleSourceUiModel>,
    selectedTab: ScheduleSourcesTabs?,
    onQueryChange: Typed1Listener<String>,
    onSourceSelected: Typed1Listener<ScheduleSourceFull>,
    onAddFavorite: Typed1Listener<ScheduleSourceFull>,
    onDeleteFavorite: Typed1Listener<ScheduleSourceFull>,
) {
    EdSearchField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        placeholder = stringResource(R.string.schedule_sou_search),
    )
    SpacerHeight(8.dp)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(filteredSources, key = { selectedTab to it.hashCode() }) { source ->
            SourceItem(
                source = source,
                onItemClick = onSourceSelected,
                onAddFavorite = onAddFavorite,
                onDeleteFavorite = onDeleteFavorite,
                modifier = Modifier.animateItemPlacement(),
            )
        }
    }
}

@Composable
private fun SourceTypeTab(
    tab: ScheduleSourcesTabs,
    isSelected: Boolean,
    onTabSelected: Typed1Listener<ScheduleSourcesTabs>,
) {
    val color = if (isSelected) {
        EdTheme.colorScheme.secondaryContainer
    } else {
        EdTheme.colorScheme.surface
    }

    val title = when (tab) {
        ScheduleSourcesTabs.Favorite -> "Избранное"
        ScheduleSourcesTabs.Group -> "Группы"
        ScheduleSourcesTabs.Teacher -> "Преподаватели"
        ScheduleSourcesTabs.Student -> "Студенты"
        ScheduleSourcesTabs.Place -> "Места занятий"
        ScheduleSourcesTabs.Subject -> "Предметы"
        ScheduleSourcesTabs.Complex -> "Расширенный поиск"
    }

    EdCard(
        onClick = { onTabSelected(tab) },
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = EdTheme.shapes.small,
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        )
    }
}

@Composable
fun SourceItem(
    source: ScheduleSourceUiModel,
    onItemClick: Typed1Listener<ScheduleSourceFull>,
    onAddFavorite: Typed1Listener<ScheduleSourceFull>,
    onDeleteFavorite: Typed1Listener<ScheduleSourceFull>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .clickable(onClick = { onItemClick(source.source) })
            .fillMaxWidth(),
    ) {
        Row(Modifier.padding(vertical = 5.dp)) {
            SpacerWidth(16.dp)
            val initials = when (source.source.type) {
                ScheduleSources.Group -> source.source.title.split('-')
                    .joinToString(separator = "") { it.take(1) }
                ScheduleSources.Teacher -> source.source.title.split(' ')
                    .joinToString(separator = "") { it.take(1) }
                ScheduleSources.Student -> source.source.title.split(' ')
                    .joinToString(separator = "") { it.take(1) }
                ScheduleSources.Place -> source.source.title
                ScheduleSources.Subject -> source.source.title.split(' ')
                    .joinToString(separator = "") { it.take(1) }
                ScheduleSources.Complex -> source.source.title.split(' ')
                    .joinToString(separator = "") { it.take(1) }
            }
            InitialAvatar(url = source.source.avatarUrl ?: "", initials)
            SpacerWidth(8.dp)
            Column(Modifier.weight(1f)) {
                Text(
                    text = source.source.title,
                    style = EdTheme.typography.titleSmall,
                )
                WithContentAlpha(alpha = ContentAlpha.medium) {
                    Text(
                        text = source.source.description,
                        style = EdTheme.typography.bodySmall,
                    )
                }
            }
            IconButton(
                onClick = {
                    if (source.isFavorite) {
                        onDeleteFavorite(source.source)
                    } else {
                        onAddFavorite(source.source)
                    }
                },
            ) {
                val tintColor = if (source.isFavorite) {
                    EdTheme.colorScheme.primary
                } else {
                    LocalContentColor.current
                }
                val painter = if (source.isFavorite) {
                    painterResource(FluentIcons.ic_fluent_star_24_filled)
                } else {
                    painterResource(FluentIcons.ic_fluent_star_24_regular)
                }
                Icon(
                    painter = painter,
                    contentDescription = null,
                    tint = tintColor,
                )
            }
            SpacerWidth(16.dp)
        }
    }
}
