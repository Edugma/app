package io.edugma.features.account.people.teachers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.divider.EdDivider
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.loader.EdLoader
import io.edugma.core.designSystem.atoms.loader.EdLoaderSize
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.molecules.avatar.EdAvatar
import io.edugma.core.designSystem.molecules.avatar.EdAvatarSize
import io.edugma.core.designSystem.molecules.avatar.toAvatarInitials
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.nothingFound.EdNothingFound
import io.edugma.core.designSystem.organism.refresher.Refresher
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.core.ui.screen.BottomSheet
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.isNotNull
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.model.departments
import io.edugma.domain.account.model.description
import io.edugma.features.account.R
import io.edugma.features.account.people.common.bottomSheets.SearchBottomSheet
import io.edugma.features.account.people.common.items.PeopleItem
import io.edugma.features.account.people.common.items.PeopleItemPlaceholder
import io.edugma.features.base.elements.*
import kotlinx.coroutines.launch

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
                            TeacherBottomSheet(
                                teacher = it,
                                openSchedule = viewModel::openTeacherSchedule,
                            )
                        }
                    }
                    BottomType.Search -> {
                        SearchBottomSheet(
                            hint = "ФИО преподавателя",
                            searchValue = state.name,
                            onSearchValueChanged = viewModel::setName,
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
fun TeacherBottomSheet(teacher: Teacher, openSchedule: ClickListener) {
    BottomSheet {
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            EdLabel(
                text = teacher.name,
                style = EdTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(0.7f),
            )
            SpacerWidth(width = 10.dp)
            EdAvatar(
                url = teacher.avatar,
                initials = teacher.name.toAvatarInitials(),
                size = EdAvatarSize(
                    size = 80.dp,
                    textSizes = listOf(21.sp, 25.sp, 25.sp, 23.sp, 22.sp, 21.sp),
                ),
            )
        }
        SpacerHeight(height = 5.dp)
        EdLabel(
            text = teacher.departments,
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(height = 10.dp)
        EdDivider(thickness = 1.dp)
        SpacerHeight(height = 10.dp)
        teacher.stuffType?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(id = EdIcons.ic_fluent_book_24_regular),
                style = EdTheme.typography.bodyMedium,
            )
            SpacerHeight(height = 7.dp)
        }
        teacher.grade?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(id = R.drawable.acc_ic_teacher_24),
                style = EdTheme.typography.bodyMedium,
            )
            SpacerHeight(height = 7.dp)
        }
        teacher.sex?.let {
            EdLabel(
                text = "Пол: $it",
                iconPainter = painterResource(id = EdIcons.ic_fluent_people_24_regular),
                style = EdTheme.typography.bodyMedium,
            )
            SpacerHeight(height = 7.dp)
        }
        if (!teacher.email.isNullOrEmpty()) {
            SelectionContainer {
                EdLabel(
                    text = teacher.email!!,
                    iconPainter = painterResource(id = EdIcons.ic_fluent_mail_24_regular),
                    style = EdTheme.typography.bodyMedium,
                )
            }
            SpacerHeight(height = 7.dp)
        }
        SpacerHeight(height = 10.dp)
        EdButton(
            text = "Посмотреть расписание",
            onClick = openSchedule,
            modifier = Modifier.fillMaxWidth(),
        )
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
                        retryAction = studentListItems::refresh,
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
    teacherClick: Typed1Listener<Teacher>,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            count = teacherListItems.itemCount,
            key = teacherListItems.itemKey { it.id },
            contentType = teacherListItems.itemContentType { "teacher" },
        ) {
            val item = teacherListItems[it]
            item?.let { teacher ->
                PeopleItem(
                    title = teacher.name,
                    description = teacher.description,
                    avatar = teacher.avatar,
                    onClick = { teacherClick.invoke(teacher) }
                        .takeIf { teacher.avatar.isNotNull() || teacher.sex.isNotNull() || teacher.description.isNotEmpty() },
                )
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
                            size = EdLoaderSize.medium,
                        )
                    }
                }
            }
            teacherListItems.loadState.append is LoadState.Error -> {
                item { Refresher(onClick = teacherListItems::retry) }
            }
        }
    }
}
