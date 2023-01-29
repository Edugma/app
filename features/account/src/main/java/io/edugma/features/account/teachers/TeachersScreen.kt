package io.edugma.features.account.teachers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.textField.EdTextField
import io.edugma.core.designSystem.organism.nothingFound.EdNothingFound
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.model.departments
import io.edugma.domain.account.model.description
import io.edugma.features.account.R
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
    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        scrimColor = Color.Black.copy(alpha = 0.5f),
        sheetBackgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        sheetContent = {
            when (state.bottomType) {
                BottomType.Teacher -> {
                    state.selectedEntity?.let {
                        TeacherInfoBottom(teacher = it)
                    }
                }
                BottomType.Search -> {
                    TeacherSearchBottom(state = state, nameListener = viewModel::setName) {
                        viewModel.load(state.name)
                        scope.launch { bottomState.hide() }
                    }
                }
            }
        },
    ) {
        TeachersContent(
            state,
            backListener = viewModel::exit,
            openBottomListener = {
                viewModel.openSearch()
                scope.launch { bottomState.show() }
            },
            openTeacher = {
                viewModel.openTeacher(it)
                scope.launch { bottomState.show() }
            },
        )
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
                modifier = Modifier.fillMaxWidth().clip(CircleShape),
            )
        }
        SpacerHeight(height = 3.dp)
        Text(
            text = teacher.departments,
            style = MaterialTheme.typography.bodyMedium,
            color = EdTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(horizontal = 8.dp),
        )
        SpacerHeight(height = 10.dp)
        teacher.sex?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(id = FluentIcons.ic_fluent_people_24_regular),
            )
        }
        teacher.grade?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(id = FluentIcons.ic_fluent_book_24_regular),
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
                iconPainter = painterResource(id = FluentIcons.ic_fluent_calendar_ltr_24_regular),
            )
        }
        if (!teacher.email.isNullOrEmpty()) {
            EdLabel(
                text = teacher.email!!,
                iconPainter = painterResource(id = FluentIcons.ic_fluent_mail_24_regular),
            )
        }
        SpacerHeight(height = 10.dp)
    }
}

@Composable
fun TeacherSearchBottom(
    state: TeachersState,
    nameListener: Typed1Listener<String>,
    searchListener: ClickListener,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp),
    ) {
        SpacerHeight(height = 15.dp)
        Text(
            text = "Поиск",
            style = EdTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 8.dp),
        )
        SpacerHeight(height = 20.dp)
        EdTextField(
            value = state.name,
            placeholder = "ФИО преподавателя",
            onValueChange = nameListener,
        )
        SpacerHeight(height = 40.dp)
        EdButton(
            onClick = searchListener,
            modifier = Modifier.fillMaxWidth(),
            text = "Применить",
        )
        SpacerHeight(height = 15.dp)
    }
}

@Composable
fun TeachersContent(
    state: TeachersState,
    openBottomListener: ClickListener,
    backListener: ClickListener,
    openTeacher: Typed1Listener<Teacher>,
) {
    val teacherListItems = state.pagingData?.collectAsLazyPagingItems()
    Column {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (content, filter) = createRefs()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(content) {
                    linkTo(parent.start, filter.start)
                    width = Dimension.fillToConstraints
                },
            ) {
                IconButton(onClick = backListener) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Назад",
                    )
                }
                SpacerWidth(width = 15.dp)
                Text(
                    text = "Преподаватели",
                    style = EdTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            IconButton(
                onClick = openBottomListener,
                modifier = Modifier.constrainAs(filter) {
                    linkTo(parent.top, parent.bottom)
                    end.linkTo(parent.end)
                },
            ) {
                Icon(
                    painterResource(id = FluentIcons.ic_fluent_search_24_regular),
                    contentDescription = "Фильтр",
                )
            }
        }
        LazyColumn {
            teacherListItems?.let { _ ->
                items(teacherListItems) { item ->
                    item?.let {
                        Teacher(teacher = it) {
                            openTeacher.invoke(it)
                        }
                    }
                }
                when {
                    teacherListItems.loadState.refresh is LoadState.Loading -> {
                        items(3) {
                            TeacherPlaceholder()
                        }
                    }
                    teacherListItems.loadState.refresh is LoadState.Error -> {
                        item { ErrorView(retryAction = teacherListItems::refresh) }
                    }
                    teacherListItems.loadState.append is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 70.dp),
                            ) {
                                androidx.compose.material3.CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .align(Alignment.Center),
                                )
                            }
                        }
                    }
                    teacherListItems.loadState.append is LoadState.Error -> {
                        item { Refresher(onClickListener = teacherListItems::retry) }
                    }
                    teacherListItems.itemCount == 0 && (teacherListItems.loadState.append.endOfPaginationReached) -> {
                        item { EdNothingFound() }
                    }
                }
            }
        }
    }
}

@Composable
fun Teacher(
    teacher: Teacher,
    onClick: ClickListener,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable(onClick = onClick),
    ) {
        Row {
            AsyncImage(
                model = teacher.avatar,
                contentDescription = "avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(70.dp),
            )
            SpacerWidth(width = 10.dp)
            Column {
                Text(
                    text = teacher.name,
                    style = EdTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                SpacerHeight(height = 3.dp)
                WithContentAlpha(alpha = ContentAlpha.medium) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = FluentIcons.ic_fluent_info_16_regular),
                            contentDescription = null,
                            modifier = Modifier
                                .size(17.dp),
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = teacher.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    }
                }
            }
        }
        androidx.compose.material3.Divider(modifier = Modifier.padding(start = 75.dp, top = 2.dp))
    }
}

@Composable
fun TeacherPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Row {
            AsyncImage(
                model = null,
                contentDescription = "avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(70.dp)
                    .placeholder(true),
            )
            SpacerWidth(width = 10.dp)
            Column {
                Text(
                    text = "",
                    style = EdTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.placeholder(true).widthIn(min = 100.dp),
                )
                SpacerHeight(height = 3.dp)
                WithContentAlpha(alpha = ContentAlpha.medium) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = FluentIcons.ic_fluent_info_16_regular),
                            contentDescription = null,
                            modifier = Modifier
                                .size(17.dp),
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .placeholder(true),
                        )
                    }
                }
            }
        }
        androidx.compose.material3.Divider(modifier = Modifier.padding(start = 75.dp, top = 2.dp))
    }
}
