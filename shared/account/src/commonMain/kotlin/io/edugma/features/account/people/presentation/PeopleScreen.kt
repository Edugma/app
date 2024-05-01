package io.edugma.features.account.people.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.edugma.core.arch.mvi.viewmodel.rememberOnAction
import io.edugma.core.designSystem.atoms.spacer.NavigationBarSpacer
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.organism.bottomSheet.ModalBottomSheetValue
import io.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import io.edugma.core.designSystem.organism.lceScaffold.EdLceScaffold
import io.edugma.core.designSystem.organism.shortInfoSheet.EdShortInfoSheet
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.pagination.PagingFooter
import io.edugma.core.ui.screen.FeatureBottomSheetScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.account.domain.model.peoples.Person
import io.edugma.features.account.people.PeopleScreenType
import io.edugma.features.account.people.common.bottomSheets.SearchBottomSheet
import io.edugma.features.account.people.common.items.PeopleItem
import io.edugma.features.account.people.common.items.PeopleItemPlaceholder
import kotlinx.coroutines.launch

@Composable
fun PeopleScreen(
    viewModel: PeopleViewModel = getViewModel(),
    type: PeopleScreenType,
) {
    LaunchedEffect(type) {
        viewModel.onArgs(type)
    }
    val state by viewModel.stateFlow.collectAsState()
    val onAction = viewModel.rememberOnAction()

    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val scope = rememberCoroutineScope()
    if (state.type != null) {
        FeatureBottomSheetScreen(
            statusBarPadding = false,
            navigationBarPadding = false,
            sheetState = bottomState,
            sheetContent = {
                when (state.bottomType) {
                    BottomSheetType.Filter -> {
                        SearchBottomSheet(
                            hint = state.type!!.queryHint,
                            searchValue = state.name,
                            onSearchValueChanged = {
                                onAction(PeopleAction.OnQuery(it))
                            },
                        ) {
                            viewModel.onSearch()
                            scope.launch { bottomState.hide() }
                        }
                    }
                    BottomSheetType.Person -> {
                        state.selectedPerson?.let {
                            PersonBottomSheet(
                                person = it,
                                openSchedule = viewModel::openSchedule,
                            )
                        }
                    }
                }
            },
        ) {
            PeopleListContent(
                state = state,
                backListener = viewModel::exit,
                openBottomSheetListener = {
                    viewModel.onSelectFilter()
                    scope.launch { bottomState.show() }
                },
                onPersonClick = {
                    viewModel.onSelectPerson(it)
                    scope.launch { bottomState.show() }
                },
                onShare = viewModel::onShare,
                onAction = onAction,
            )
        }
    }
}

@Composable
fun ColumnScope.PersonBottomSheet(
    person: Person,
    openSchedule: () -> Unit,
) {
    EdShortInfoSheet(
        title = person.name,
        avatar = person.avatar,
        description = person.description.orEmpty(),
    ) {
        EdButton(
            text = "Посмотреть расписание",
            onClick = openSchedule,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun PeopleListContent(
    state: PeopleUiState,
    backListener: ClickListener,
    openBottomSheetListener: ClickListener,
    onPersonClick: Typed1Listener<Person>,
    onShare: ClickListener,
    onAction: (PeopleAction) -> Unit,
) {
    Column(Modifier) {
        EdSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
        ) {
            EdTopAppBar(
                title = state.type!!.title,
                onNavigationClick = backListener,
                windowInsets = WindowInsets.statusBars,
                actions = {
                    val students = state.paginationState.items

                    IconButton(
                        onClick = { onShare() },
                        enabled = students.isNotEmpty(),
                    ) {
                        Icon(
                            painterResource(EdIcons.ic_fluent_share_24_regular),
                            contentDescription = "Поделиться",
                        )
                    }
                    IconButton(onClick = openBottomSheetListener) {
                        Icon(
                            painterResource(EdIcons.ic_fluent_search_24_regular),
                            contentDescription = "Фильтр",
                        )
                    }
                },
            )
        }
        EdLceScaffold(
            lceState = state.paginationState.lceState,
            onRefresh = { onAction(PeopleAction.OnRefresh) },
            placeholder = { PeopleListPlaceholder() },
        ) {
            PeopleList(
                state = state,
                studentClick = onPersonClick,
                onAction = onAction,
            )
        }
    }
}

@Composable
internal fun PeopleList(
    state: PeopleUiState,
    studentClick: Typed1Listener<Person>,
    onAction: (PeopleAction) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (state.paginationState.items.isNotEmpty()) {
            items(
                count = state.paginationState.items.size,
                key = { state.paginationState.items[it].id },
                contentType = { "people" },
            ) {
                val item = state.paginationState.items[it]
                item.let {
                    PeopleItem(
                        title = it.name,
                        description = it.description.orEmpty(),
                        avatar = it.avatar,
                    ) { studentClick.invoke(it) }
                }
            }
        }
        item {
            PagingFooter(state.paginationState) {
                onAction(PeopleAction.OnLoadNextPage)
            }
        }
        item {
            NavigationBarSpacer(10.dp)
        }
    }
}

@Composable
internal fun PeopleListPlaceholder() {
    Column(modifier = Modifier.fillMaxSize()) {
        repeat(6) {
            PeopleItemPlaceholder()
        }
    }
}
