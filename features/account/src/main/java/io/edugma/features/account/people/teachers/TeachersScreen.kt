package io.edugma.features.account.people.teachers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.loader.EdLoader
import io.edugma.core.designSystem.atoms.loader.EdLoaderSize
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.nothingFound.EdNothingFound
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.model.departments
import io.edugma.domain.account.model.description
import io.edugma.features.account.R
import io.edugma.features.account.people.common.bottomSheets.SearchBottomSheet
import io.edugma.features.account.people.common.items.PeopleItem
import io.edugma.features.account.people.common.items.PeopleItemPlaceholder
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TeachersScreen(viewModel: TeachersViewModel = getViewModel()) {
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
                when (state.bottomType) {
                    BottomType.Teacher -> {
                        state.selectedEntity?.let {
                            TeacherInfoBottom(teacher = it)
                        }
                    }
                    BottomType.Search -> {
                        SearchBottomSheet(
                            hint = "ФИО преподавателя",
                            searchValue = state.name,
                            onSearchValueChanged = viewModel::setName
                        ) {
                            viewModel.load(state.name)
                            scope.launch { bottomState.hide() }
                        }
                    }
                }
            },
        ) {
            TeachersListContent(
                state,
                backListener = viewModel::exit,
                openBottomSheetListener = {
                    viewModel.openSearch()
                    scope.launch { bottomState.show() }
                },
                teacherClick = {
                    viewModel.openTeacher(it)
                    scope.launch { bottomState.show() }
                },
            )
        }
    }
}

@Composable
fun TeacherInfoBottom(teacher: Teacher) {
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp),
    ) {
        SpacerHeight(height = 15.dp)
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = teacher.name,
                style = EdTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            AsyncImage(
                model = teacher.avatar,
                contentDescription = "avatar",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape),
            )
        }
        SpacerHeight(height = 3.dp)
        Text(
            text = teacher.departments,
            style = EdTheme.typography.bodyMedium,
            color = EdTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(horizontal = 8.dp),
        )
        SpacerHeight(height = 10.dp)
        teacher.sex?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(id = EdIcons.ic_fluent_people_24_regular),
            )
        }
        teacher.grade?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(id = EdIcons.ic_fluent_book_24_regular),
            )
        }
        teacher.stuffType?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(id = R.drawable.acc_ic_teacher_24),
            )
        }
        teacher.birthday?.let {
            EdLabel(
                text = it.format(),
                iconPainter = painterResource(id = EdIcons.ic_fluent_calendar_ltr_24_regular),
            )
        }
        if (!teacher.email.isNullOrEmpty()) {
            EdLabel(
                text = teacher.email!!,
                iconPainter = painterResource(id = EdIcons.ic_fluent_mail_24_regular),
            )
        }
        SpacerHeight(height = 10.dp)
    }
}
@Composable
fun TeachersListContent(
    state: TeachersState,
    backListener: ClickListener,
    openBottomSheetListener: ClickListener,
    teacherClick: Typed1Listener<Teacher>,
) {
    val studentListItems = state.pagingData?.collectAsLazyPagingItems()
    Column(Modifier.navigationBarsPadding()) {
        EdTopAppBar(
            title = "Преподаватели",
            onNavigationClick = backListener,
            actions = {
                IconButton(onClick = openBottomSheetListener) {
                    Icon(
                        painterResource(id = EdIcons.ic_fluent_search_24_regular),
                        contentDescription = "Фильтр",
                    )
                }
            },
        )
        studentListItems?.let {
            when {
                studentListItems.loadState.refresh is LoadState.Error -> {
                    ErrorWithRetry(
                        modifier = Modifier.fillMaxSize(),
                        retryAction = studentListItems::refresh
                    )
                }
                studentListItems.itemCount == 0 && studentListItems.loadState.append.endOfPaginationReached -> {
                    EdNothingFound(modifier = Modifier.fillMaxSize())
                }
                else -> { TeachersList(studentListItems, teacherClick) }
            }
        }
    }
}

@Composable
fun TeachersList(
    teacherListItems: LazyPagingItems<Teacher>,
    teacherClick: Typed1Listener<Teacher>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(teacherListItems) { item ->
            item?.let {
                PeopleItem(it.name, it.description, it.avatar) { teacherClick.invoke(it) }
            }
        }
        when {
            teacherListItems.loadState.refresh is LoadState.Loading -> {
                items(3) {
                    PeopleItemPlaceholder()
                }
            }
            teacherListItems.loadState.append is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 70.dp),
                    ) {
                        EdLoader(
                            modifier = Modifier
                                .align(Alignment.Center),
                            size = EdLoaderSize.medium
                        )
                    }
                }
            }
            teacherListItems.loadState.append is LoadState.Error -> {
                item { Refresher(onClickListener = teacherListItems::retry) }
            }
        }
    }
}
