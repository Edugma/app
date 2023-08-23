package io.edugma.features.account.people.students

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.moriatsushi.insetsx.navigationBarsPadding
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.api.utils.format
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
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.nothingFound.EdNothingFound
import io.edugma.core.designSystem.organism.refresher.Refresher
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.rememberAsyncImagePainter
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.BottomSheet
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.features.account.domain.model.student.Student
import io.edugma.features.account.people.common.bottomSheets.SearchBottomSheet
import io.edugma.features.account.people.common.items.PeopleItem
import io.edugma.features.account.people.common.items.PeopleItemPlaceholder
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudentsScreen(viewModel: StudentsViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()

    val studentListItems = remember {
        viewModel.stateFlow.map { it.pagingData }.filterNotNull()
    }.collectAsLazyPagingItems()

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
                    BottomSheetType.Filter -> {
                        SearchBottomSheet(
                            hint = "Введите фамилию или группу",
                            searchValue = state.name,
                            onSearchValueChanged = viewModel::setName,
                        ) {
                            viewModel.load(state.name)
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
                studentListItems = studentListItems,
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
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StudentBottomSheet(student: Student) {
    BottomSheet {
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            EdLabel(
                text = student.getFullName(),
                style = EdTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(0.7f),
            )
            SpacerWidth(width = 10.dp)
            EdAvatar(
                url = student.avatar,
                initials = student.getFullName().toAvatarInitials(),
                size = EdAvatarSize(
                    size = 80.dp,
                    textSizes = listOf(21.sp, 25.sp, 25.sp, 23.sp, 22.sp, 21.sp),
                ),
            )
        }
        SpacerHeight(height = 20.dp)
        EdLabel(
            iconPainter = painterResource(EdIcons.ic_fluent_building_24_regular),
            text = student.branch.title,
            style = EdTheme.typography.bodyLarge,
        )
        SpacerHeight(height = 12.dp)
        EdLabel(
            iconPainter = painterResource(EdIcons.ic_fluent_building_24_regular),
            text = "${student.course} курс, ${student.educationType.lowercase()}",
            style = EdTheme.typography.bodyLarge,
        )
        SpacerHeight(height = 12.dp)
        EdLabel(
            iconPainter = painterResource(EdIcons.ic_fluent_people_24_regular),
            text = "Пол: ${student.sex}",
            style = EdTheme.typography.bodyLarge,
        )
        SpacerHeight(height = 10.dp)
        EdDivider(thickness = 1.dp)
        SpacerHeight(height = 10.dp)
        EdLabel(
            text = "Группа",
            style = EdTheme.typography.bodyLarge,
        )
        EdLabel(text = student.toString())
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StudentSheetContent(
    student: Student,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp),
    ) {
        SpacerHeight(height = 15.dp)
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = student.getFullName(),
                style = EdTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            student.avatar?.let { avatarUrl ->
                val painter = rememberAsyncImagePainter(model = avatarUrl)
                Image(
                    painter = painter,
                    contentDescription = "avatar",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape),
                )
            }
        }
        SpacerHeight(height = 3.dp)
        Text(
            text = student.getInfo(),
            style = EdTheme.typography.bodyMedium,
            color = EdTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(horizontal = 8.dp),
        )
        SpacerHeight(height = 10.dp)
        student.sex?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(EdIcons.ic_fluent_people_24_regular),
            )
        }
        EdLabel(
            text = student.branch.title,
            iconPainter = painterResource(EdIcons.ic_fluent_building_24_regular),
        )
        EdLabel(
            text = student.educationType,
            iconPainter = painterResource(EdIcons.ic_fluent_hat_graduation_24_filled),
        )
        EdLabel(
            text = student.payment,
            iconPainter = painterResource(EdIcons.ic_fluent_money_24_regular),
        )
        student.getFaculty()?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(EdIcons.ic_fluent_book_24_regular),
            )
        }
        student.group?.direction?.let {
            EdLabel(
                text = it.title,
                iconPainter = painterResource(EdIcons.ic_fluent_contact_card_group_24_regular),
            )
        }
        student.specialization?.let {
            if (it.title != student.group?.direction?.title) {
                EdLabel(
                    text = it.title,
                    iconPainter = painterResource(EdIcons.ic_fluent_data_treemap_24_regular),
                )
            }
        }
        EdLabel(
            text = "Года обучения: ${student.years}",
            iconPainter = painterResource(EdIcons.ic_fluent_timer_24_regular),
        )
        student.dormitory?.let {
            EdLabel(
                text = "Общежитие №$it",
                iconPainter = painterResource(EdIcons.ic_fluent_building_home_24_regular),
            )
        }
        student.dormitoryRoom?.let {
            EdLabel(
                text = "Комната №$it",
                iconPainter = painterResource(EdIcons.ic_fluent_conference_room_24_regular),
            )
        }
        student.birthday?.let {
            EdLabel(
                text = "Дата рождения: ${it.format()}",
                iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_24_regular),
            )
        }
//        Text(text = student.toString())
        SpacerHeight(height = 10.dp)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StudentsListContent(
    state: StudentsState,
    studentListItems: LazyPagingItems<Student>,
    backListener: ClickListener,
    openBottomSheetListener: ClickListener,
    studentClick: Typed1Listener<Student>,
    onShare: ClickListener,
) {
    Column(Modifier.navigationBarsPadding()) {
        EdTopAppBar(
            title = "Студенты",
            onNavigationClick = backListener,
            actions = {
                val students = studentListItems.itemSnapshotList?.items

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
        studentListItems?.let {
            when {
                studentListItems.loadState.refresh is LoadStateError -> {
                    ErrorWithRetry(
                        modifier = Modifier.fillMaxSize(),
                        retryAction = studentListItems::refresh,
                    )
                }
                studentListItems.itemCount == 0 && studentListItems.loadState.append.endOfPaginationReached -> {
                    EdNothingFound(modifier = Modifier.fillMaxSize())
                }
                else -> { StudentsList(studentListItems, studentClick) }
            }
        }
    }
}

@Composable
fun StudentsList(
    studentListItems: LazyPagingItems<Student>,
    studentClick: Typed1Listener<Student>,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            count = studentListItems.itemCount,
            key = studentListItems.itemKey { it.id },
            contentType = studentListItems.itemContentType { "student" },
        ) {
            val item = studentListItems[it]
            item?.let {
                PeopleItem(it.getFullName(), it.getInfo(), it.avatar) { studentClick.invoke(it) }
            }
        }
        when {
            studentListItems.loadState.refresh is LoadStateLoading -> {
                items(3) {
                    PeopleItemPlaceholder()
                }
            }
            studentListItems.loadState.append is LoadStateLoading -> {
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
            studentListItems.loadState.append is LoadStateError -> {
                item { Refresher(onClick = studentListItems::retry) }
            }
        }
    }
}
