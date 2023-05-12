package io.edugma.features.account.people.students

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
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
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.core.ui.screen.BottomSheet
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.domain.account.model.student.Student
import io.edugma.features.account.R
import io.edugma.features.account.people.common.bottomSheets.SearchBottomSheet
import io.edugma.features.account.people.common.items.PeopleItem
import io.edugma.features.account.people.common.items.PeopleItemPlaceholder
import io.edugma.features.account.people.common.utlis.convertAndShare
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudentsScreen(viewModel: StudentsViewModel = getViewModel()) {
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
                    BottomSheetType.Filter -> {
                        SearchBottomSheet(
                            hint = "Введите фамилию или группу",
                            searchValue = state.name,
                            onSearchValueChanged = viewModel::setName
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
                backListener = viewModel::exit,
                openBottomSheetListener = {
                    viewModel.selectFilter()
                    scope.launch { bottomState.show() }
                },
                studentClick = {
                    viewModel.selectStudent(it)
                    scope.launch { bottomState.show() }
                },
            )
        }
    }
}

@Composable
fun StudentBottomSheet(student: Student) {
    BottomSheet{
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EdLabel(
                text = student.getFullName(),
                style = EdTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(0.7f)
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
            iconPainter = painterResource(id = EdIcons.ic_fluent_building_24_regular),
            text = student.branch.title,
            style = EdTheme.typography.bodyLarge
        )
        SpacerHeight(height = 12.dp)
        EdLabel(
            iconPainter = painterResource(id = EdIcons.ic_fluent_building_24_regular),
            text = "${student.course} курс, ${student.educationType.lowercase()}",
            style = EdTheme.typography.bodyLarge
        )
        SpacerHeight(height = 12.dp)
        EdLabel(
            iconPainter = painterResource(id = EdIcons.ic_fluent_people_24_regular),
            text = "Пол: ${student.sex}",
            style = EdTheme.typography.bodyLarge
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
            AsyncImage(
                model = student.avatar,
                contentDescription = "avatar",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape),
            )
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
                iconPainter = painterResource(id = EdIcons.ic_fluent_people_24_regular),
            )
        }
        EdLabel(
            text = student.branch.title,
            iconPainter = painterResource(id = EdIcons.ic_fluent_building_24_regular),
        )
        EdLabel(
            text = student.educationType,
            iconPainter = painterResource(id = R.drawable.acc_ic_teacher_24),
        )
        EdLabel(
            text = student.payment,
            iconPainter = painterResource(id = EdIcons.ic_fluent_money_24_regular),
        )
        student.getFaculty()?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(id = EdIcons.ic_fluent_book_24_regular),
            )
        }
        student.group?.direction?.let {
            EdLabel(
                text = it.title,
                iconPainter = painterResource(id = EdIcons.ic_fluent_contact_card_group_24_regular),
            )
        }
        student.specialization?.let {
            if (it.title != student.group?.direction?.title) {
                EdLabel(
                    text = it.title,
                    iconPainter = painterResource(id = EdIcons.ic_fluent_data_treemap_24_regular),
                )
            }
        }
        EdLabel(
            text = "Года обучения: ${student.years}",
            iconPainter = painterResource(id = EdIcons.ic_fluent_timer_24_regular),
        )
        student.dormitory?.let {
            EdLabel(
                text = "Общежитие №$it",
                iconPainter = painterResource(id = EdIcons.ic_fluent_building_home_24_regular),
            )
        }
        student.dormitoryRoom?.let {
            EdLabel(
                text = "Комната №$it",
                iconPainter = painterResource(id = EdIcons.ic_fluent_conference_room_24_regular),
            )
        }
        student.birthday?.let {
            EdLabel(
                text = "Дата рождения: ${it.format()}",
                iconPainter = painterResource(id = EdIcons.ic_fluent_calendar_ltr_24_regular),
            )
        }
//        Text(text = student.toString())
        SpacerHeight(height = 10.dp)
    }
}

@Composable
fun StudentsListContent(
    state: StudentsState,
    backListener: ClickListener,
    openBottomSheetListener: ClickListener,
    studentClick: Typed1Listener<Student>,
) {
    val studentListItems = state.pagingData?.collectAsLazyPagingItems()
    Column(Modifier.navigationBarsPadding()) {
        EdTopAppBar(
            title = "Студенты",
            onNavigationClick = backListener,
            actions = {
                val students = studentListItems?.itemSnapshotList?.items
                val context = LocalContext.current

                IconButton(
                    onClick = { students?.convertAndShare(context) },
                    enabled = !students.isNullOrEmpty()
                ) {
                    Icon(
                        painterResource(id = EdIcons.ic_fluent_share_24_regular),
                        contentDescription = "Поделиться",
                    )
                }
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
                else -> { StudentsList(studentListItems, studentClick) }
            }
        }
    }
}

@Composable
fun StudentsList(
    studentListItems: LazyPagingItems<Student>,
    studentClick: Typed1Listener<Student>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(studentListItems) { item ->
            item?.let {
                PeopleItem(it.getFullName(), it.getInfo(), it.avatar) { studentClick.invoke(it) }
            }
        }
        when {
            studentListItems.loadState.refresh is LoadState.Loading -> {
                items(3) {
                    PeopleItemPlaceholder()
                }
            }
            studentListItems.loadState.append is LoadState.Loading -> {
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
            studentListItems.loadState.append is LoadState.Error -> {
                item { Refresher(onClickListener = studentListItems::retry) }
            }
        }
    }
}
