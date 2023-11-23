package io.edugma.features.account.people.students

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.designSystem.atoms.divider.EdDivider
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.molecules.avatar.EdAvatar
import io.edugma.core.designSystem.molecules.avatar.EdAvatarSize
import io.edugma.core.designSystem.molecules.avatar.toAvatarInitials
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
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.account.domain.model.student.Student
import io.edugma.features.account.people.common.bottomSheets.SearchBottomSheet
import io.edugma.features.account.people.common.items.PeopleItem
import io.edugma.features.account.people.common.items.PeopleItemPlaceholder
import io.edugma.features.account.people.common.paging.PagingFooter
import kotlinx.coroutines.launch

@Composable
fun StudentsScreen(viewModel: StudentsViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()

    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val scope = rememberCoroutineScope()
    FeatureBottomSheetScreen(
        navigationBarPadding = false,
        sheetState = bottomState,
        sheetContent = {
            when (state.bottomType) {
                BottomSheetType.Filter -> {
                    SearchBottomSheet(
                        hint = "Введите фамилию или группу",
                        searchValue = state.name,
                        onSearchValueChanged = viewModel::setName,
                    ) {
                        viewModel.searchRequest()
                        scope.launch { bottomState.hide() }
                    }
                }
                BottomSheetType.Student -> {
                    state.selectedStudent?.let { StudentBottomSheet(it) }
                }
            }
        },
    ) {
        StudentsListContent(
            state,
            backListener = viewModel::exit,
            openBottomSheetListener = {
                viewModel.selectFilter()
                scope.launch { bottomState.show() }
            },
            studentClick = {
                viewModel.selectStudent(it)
                scope.launch { bottomState.show() }
            },
            onShare = viewModel::onShare,
            onLoad = viewModel::loadNextPage,
        )
    }
}

@Composable
fun ColumnScope.StudentBottomSheet(student: Student) {
    BottomSheet {
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            EdLabel(
                text = student.name,
                style = EdTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(0.7f),
            )
            SpacerWidth(width = 10.dp)
            EdAvatar(
                url = student.avatar,
                initials = student.name.toAvatarInitials(),
                size = EdAvatarSize(
                    size = 80.dp,
                    textSizes = listOf(21.sp, 25.sp, 25.sp, 23.sp, 22.sp, 21.sp),
                ),
            )
        }
        SpacerHeight(height = 7.dp)
        if (student.faculty != null) {
            EdLabel(
                text = student.faculty,
                iconPainter = painterResource(EdIcons.ic_fluent_hat_graduation_24_filled),
                style = EdTheme.typography.bodyMedium,
            )
            SpacerHeight(height = 7.dp)
        }
        student.group?.let { group ->
            EdDivider(thickness = 1.dp)
            SpacerHeight(height = 10.dp)
            EdLabel(
                text = "Группа ${group.title}",
                style = EdTheme.typography.titleLarge,
            )
            SpacerHeight(height = 7.dp)
        }
    }
}

@Composable
fun StudentsListContent(
    state: StudentsState,
    backListener: ClickListener,
    openBottomSheetListener: ClickListener,
    studentClick: Typed1Listener<Student>,
    onLoad: ClickListener,
    onShare: ClickListener,
) {
    Column(Modifier.navigationBarsPadding()) {
        EdTopAppBar(
            title = "Студенты",
            onNavigationClick = backListener,
            actions = {
                val students = state.students

                IconButton(
                    onClick = { onShare() },
                    enabled = !students.isNullOrEmpty(),
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
        when {
            state.isFullscreenError -> {
                ErrorWithRetry(
                    modifier = Modifier.fillMaxSize(),
                    retryAction = onLoad,
                )
            }
            state.isNothingFound -> {
                EdNothingFound(modifier = Modifier.fillMaxSize())
            }
            else -> {
                StudentsList(state, studentClick, onLoad)
            }
        }
    }
}

@Composable
fun StudentsList(
    state: StudentsState,
    studentClick: Typed1Listener<Student>,
    loadListener: ClickListener,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (state.placeholders) {
            items(3) {
                PeopleItemPlaceholder()
            }
        } else {
            if (!state.students.isNullOrEmpty()) {
                items(
                    count = state.students.size,
                    key = { state.students[it].id },
                    contentType = { "student" },
                ) {
                    val item = state.students[it]
                    item.let {
                        PeopleItem(it.name, it.getInfo(), it.avatar) { studentClick.invoke(it) }
                    }
                }
            }
            item { PagingFooter(state.loadingState, loadListener) }
        }
    }
}
