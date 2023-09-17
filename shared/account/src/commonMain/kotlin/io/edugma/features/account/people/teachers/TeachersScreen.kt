package io.edugma.features.account.people.teachers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.divider.EdDivider
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.molecules.avatar.EdAvatar
import io.edugma.core.designSystem.molecules.avatar.EdAvatarSize
import io.edugma.core.designSystem.molecules.avatar.toAvatarInitials
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.organism.bottomSheet.ModalBottomSheetValue
import io.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.nothingFound.EdNothingFound
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.navigationBarsPadding
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.BottomSheet
import io.edugma.core.ui.screen.FeatureBottomSheetScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.isNotNull
import io.edugma.features.account.domain.model.Teacher
import io.edugma.features.account.domain.model.departments
import io.edugma.features.account.domain.model.description
import io.edugma.features.account.people.common.bottomSheets.SearchBottomSheet
import io.edugma.features.account.people.common.items.PeopleItem
import io.edugma.features.account.people.common.items.PeopleItemPlaceholder
import io.edugma.features.account.people.common.paging.PagingFooter
import kotlinx.coroutines.launch

@Composable
fun TeachersScreen(viewModel: TeachersViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()
    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    val scope = rememberCoroutineScope()

    FeatureBottomSheetScreen(
        navigationBarPadding = false,
        sheetState = bottomState,
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
                        viewModel.load()
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
            teacherClick = { teacher ->
                if (teacher.avatar.isNotNull() || teacher.sex.isNotNull() || teacher.description.isNotEmpty()) {
                    viewModel.openTeacher(teacher)
                    scope.launch { bottomState.show() }
                }
            },
            loadListener = viewModel::nextPage,
        )
    }
}

@Composable
fun ColumnScope.TeacherBottomSheet(teacher: Teacher, openSchedule: ClickListener) {
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
                iconPainter = painterResource(EdIcons.ic_fluent_book_24_regular),
                style = EdTheme.typography.bodyMedium,
            )
            SpacerHeight(height = 7.dp)
        }
        teacher.grade?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(EdIcons.ic_fluent_hat_graduation_24_filled),
                style = EdTheme.typography.bodyMedium,
            )
            SpacerHeight(height = 7.dp)
        }
        teacher.sex?.let {
            EdLabel(
                text = "Пол: $it",
                iconPainter = painterResource(EdIcons.ic_fluent_people_24_regular),
                style = EdTheme.typography.bodyMedium,
            )
            SpacerHeight(height = 7.dp)
        }
        if (!teacher.email.isNullOrEmpty()) {
            SelectionContainer {
                EdLabel(
                    text = teacher.email,
                    iconPainter = painterResource(EdIcons.ic_fluent_mail_24_regular),
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
    loadListener: ClickListener,
) {
    Column(Modifier.navigationBarsPadding()) {
        EdTopAppBar(
            title = "Преподаватели",
            onNavigationClick = backListener,
            actions = {
                IconButton(onClick = openBottomSheetListener) {
                    Icon(
                        painterResource(EdIcons.ic_fluent_search_24_regular),
                        contentDescription = "Фильтр",
                    )
                }
            },
        )
        when {
            state.isFullscreenError -> {
                ErrorWithRetry(
                    modifier = Modifier.fillMaxSize(),
                    retryAction = loadListener,
                )
            }
            state.isNothingFound -> {
                EdNothingFound(modifier = Modifier.fillMaxSize())
            }
            else -> {
                TeachersList(state, teacherClick, loadListener)
            }
        }
    }
}

@Composable
fun TeachersList(
    state: TeachersState,
    teacherClick: Typed1Listener<Teacher>,
    loadListener: ClickListener,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (state.placeholders) {
            items(3) {
                PeopleItemPlaceholder()
            }
        } else {
            if (!state.teachers.isNullOrEmpty()) {
                items(
                    count = state.teachers.size,
                    key = { it },
                    contentType = { "student" },
                ) {
                    val teacher = state.teachers[it]
                    PeopleItem(
                        title = teacher.name,
                        description = teacher.description,
                        avatar = teacher.avatar,
                        onClick = { teacherClick.invoke(teacher) },
                    )
                }
            }
            item { PagingFooter(state.loadingState, loadListener) }
        }
    }
}
