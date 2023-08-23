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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.card.EdCardDefaults
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.molecules.avatar.EdAvatar
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.searchField.EdSearchField
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.core.designSystem.utils.ContentAlpha
import io.edugma.core.designSystem.utils.WithContentAlpha
import io.edugma.core.icons.EdIcons
import io.edugma.core.resources.MR
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import io.edugma.features.schedule.domain.model.source.ScheduleSourcesTabs
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun ScheduleSourcesScreen(viewModel: ScheduleSourcesViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()

    FeatureScreen(
        statusBarPadding = false,
    ) {
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
}

@OptIn(ExperimentalMaterialApi::class)
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
                        .padding(horizontal = EdTheme.paddings.smallSecondary)
                        .fillMaxWidth(),
                    placeholder = stringResource(MR.strings.schedule_sou_search),
                )
                SpacerHeight(height = EdTheme.paddings.smallSecondary)
            }
        }
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
                filteredSources = state.filteredSources,
                selectedTab = state.selectedTab,
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
    tabs: List<ScheduleSourcesTabs>,
    selectedTab: ScheduleSourcesTabs?,
    onTabSelected: Typed1Listener<ScheduleSourcesTabs>,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = EdTheme.paddings.extraSmallTertiary),
    ) {
        items(tabs) { tab ->
            SourceTypeTab(
                tab,
                tab == selectedTab,
                onTabSelected = onTabSelected,
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
    filteredSources: List<ScheduleSourceUiModel>,
    selectedTab: ScheduleSourcesTabs?,
    onSourceSelected: Typed1Listener<ScheduleSourceFull>,
    onAddFavorite: Typed1Listener<ScheduleSourceFull>,
    onDeleteFavorite: Typed1Listener<ScheduleSourceFull>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 10.dp),
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
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
        colors = EdCardDefaults.cardColors(containerColor = color),
        shape = EdTheme.shapes.small,
    ) {
        EdLabel(
            text = title,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = EdTheme.typography.bodyMedium,
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
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
            EdAvatar(url = source.source.avatarUrl ?: "", initials = initials)
            SpacerWidth(8.dp)
            Column(Modifier.weight(1f)) {
                EdLabel(
                    text = source.source.title,
                    style = EdTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                WithContentAlpha(alpha = ContentAlpha.medium) {
                    EdLabel(
                        text = source.source.description,
                        style = EdTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
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
                    painterResource(EdIcons.ic_fluent_star_24_filled)
                } else {
                    painterResource(EdIcons.ic_fluent_star_24_regular)
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
