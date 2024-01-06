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
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.designSystem.atoms.spacer.NavigationBarSpacer
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.organism.bottomSheet.ModalBottomSheetValue
import io.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import io.edugma.core.designSystem.organism.errorWithRetry.EdErrorRetry
import io.edugma.core.designSystem.organism.nothingFound.EdNothingFound
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
                            onSearchValueChanged = viewModel::setName,
                        ) {
                            viewModel.searchRequest()
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
                    viewModel.selectFilter()
                    scope.launch { bottomState.show() }
                },
                onPersonClick = {
                    viewModel.selectPerson(it)
                    scope.launch { bottomState.show() }
                },
                onShare = viewModel::onShare,
                onLoad = viewModel::loadNextPage,
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
    state: StudentsState,
    backListener: ClickListener,
    openBottomSheetListener: ClickListener,
    onPersonClick: Typed1Listener<Person>,
    onLoad: ClickListener,
    onShare: ClickListener,
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
        when {
            state.isFullscreenError -> {
                EdErrorRetry(
                    modifier = Modifier.fillMaxSize(),
                    onRetry = onLoad,
                )
            }
            state.isNothingFound -> {
                EdNothingFound(modifier = Modifier.fillMaxSize())
            }
            else -> {
                PeopleList(state, onPersonClick, onLoad)
            }
        }
    }
}

@Composable
internal fun PeopleList(
    state: StudentsState,
    studentClick: Typed1Listener<Person>,
    loadListener: ClickListener,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (state.placeholders) {
            items(3) {
                PeopleItemPlaceholder()
            }
        } else {
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
            item { PagingFooter(state.paginationState, loadListener) }
            item {
                NavigationBarSpacer(10.dp)
            }
        }
    }
}
