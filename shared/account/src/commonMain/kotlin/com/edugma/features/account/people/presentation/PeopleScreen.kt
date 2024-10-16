package com.edugma.features.account.people.presentation

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.edugma.core.arch.mvi.viewmodel.rememberOnAction
import com.edugma.core.designSystem.atoms.spacer.NavigationBarSpacer
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.molecules.button.EdButton
import com.edugma.core.designSystem.organism.bottomSheet.bind
import com.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import com.edugma.core.designSystem.organism.lceScaffold.EdLceScaffold
import com.edugma.core.designSystem.organism.shortInfoSheet.EdShortInfoSheet
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.icons.EdIcons
import com.edugma.core.ui.pagination.PagingFooter
import com.edugma.core.ui.screen.FeatureBottomSheetScreen
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.ClickListener
import com.edugma.core.utils.Typed1Listener
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel
import com.edugma.features.account.domain.model.peoples.Person
import com.edugma.features.account.people.PeopleScreenType
import com.edugma.features.account.people.common.bottomSheets.SearchBottomSheet
import com.edugma.features.account.people.common.items.PeopleItem
import com.edugma.features.account.people.common.items.PeopleItemPlaceholder
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun PeopleScreen(
    viewModel: PeopleViewModel = getViewModel(),
    type: PeopleScreenType,
) {
    val state by viewModel.collectAsState()
    val onAction = viewModel.rememberOnAction()

    LaunchedEffect(type) {
        onAction(PeopleAction.OnArgs(type))
    }

    val bottomState = rememberModalBottomSheetState()
    bottomState.bind(
        showBottomSheet = { state.showBottomSheet },
        onClosed = { onAction(PeopleAction.OnBottomSheetClosed) }
    )
    if (state.type != null) {
        FeatureScreen(
            statusBarPadding = false,
            navigationBarPadding = false,
        ) {
            PeopleListContent(
                state = state,
                openBottomSheetListener = {
                    onAction(PeopleAction.OnSelectFilter)
                },
                onPersonClick = {
                    onAction(PeopleAction.OnSelectPerson(it))
                },
                onAction = onAction,
            )
        }

        FeatureBottomSheetScreen(
            sheetState = bottomState,
        ) {
            when (state.bottomType) {
                BottomSheetType.Filter -> {
                    SearchBottomSheet(
                        hint = state.type!!.queryHint,
                        searchValue = state.name,
                        onSearchValueChanged = {
                            onAction(PeopleAction.OnQuery(it))
                        },
                    ) {
                        onAction(PeopleAction.OnSearchClick)
                    }
                }
                BottomSheetType.Person -> {
                    state.selectedPerson?.let {
                        PersonBottomSheet(
                            person = it,
                            openSchedule = {
                                onAction(PeopleAction.OnOpenSchedule)
                            },
                        )
                    }
                }
            }
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
    openBottomSheetListener: ClickListener,
    onPersonClick: Typed1Listener<Person>,
    onAction: (PeopleAction) -> Unit,
) {
    Column(Modifier) {
        EdSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
        ) {
            EdTopAppBar(
                title = state.type!!.title,
                onNavigationClick = {
                    onAction(PeopleAction.Back)
                },
                windowInsets = WindowInsets.statusBars,
                actions = {
                    val students = state.paginationState.items

                    IconButton(
                        onClick = { onAction(PeopleAction.OnShareClick) },
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
