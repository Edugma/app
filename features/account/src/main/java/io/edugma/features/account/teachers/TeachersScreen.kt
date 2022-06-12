package io.edugma.features.account.teachers

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.model.description
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.core.utils.ContentAlpha
import io.edugma.features.base.elements.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TeachersScreen(viewModel: TeachersViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()
    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        scrimColor = Color.Black.copy(alpha = 0.5f),
        sheetBackgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        sheetContent = {
            when(state.bottomType) {
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
        }
    ) {
        TeachersContent(
            state,
            backListener = { viewModel.exit() },
            openBottomListener = { scope.launch {bottomState.show() } },
            openTeacher = {
                viewModel.openTeacher(it)
                scope.launch { bottomState.show() }
            }
        )
    }
}

@Composable
fun TeacherInfoBottom(teacher: Teacher) {
    Text(text = teacher.toString())
}

@Composable
fun TeacherSearchBottom(
    state: TeachersState,
    nameListener: Typed1Listener<String>,
    searchListener: ClickListener
) {
    Column(modifier = Modifier
        .padding(horizontal = 15.dp)) {
        SpacerHeight(height = 15.dp)
        Text(
            text = "Поиск",
            style = MaterialTheme3.typography.headlineMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
        SpacerHeight(height = 20.dp)
        TextBox(
            value = state.name,
            title = "ФИО преподавателя",
            onValueChange = nameListener)
        SpacerHeight(height = 40.dp)
        PrimaryButton(
            onClick = searchListener,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Применить")
        }
        SpacerHeight(height = 15.dp)
    }
}

@Composable
fun TeachersContent(
    state: TeachersState,
    openBottomListener: ClickListener,
    backListener: ClickListener,
    openTeacher: Typed1Listener<Teacher>
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
                }) {
                IconButton(onClick = backListener) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
                SpacerWidth(width = 15.dp)
                Text(
                    text = "Преподаватели",
                    style = MaterialTheme3.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(onClick = openBottomListener, modifier = Modifier.constrainAs(filter) {
                linkTo(parent.top, parent.bottom)
                end.linkTo(parent.end)
            }) {
                Icon(
                    painterResource(id = FluentIcons.ic_fluent_search_24_regular),
                    contentDescription = "Фильтр"
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
                        item { Text(text = "placeholders") }
                    }
                    teacherListItems.loadState.refresh is LoadState.Error -> {
                        item { ErrorView(retryAction = teacherListItems::refresh) }
                    }
                    teacherListItems.loadState.append is LoadState.Loading -> {
                        item {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 70.dp)) {
                                androidx.compose.material3.CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                    teacherListItems.loadState.append is LoadState.Error -> {
                        item { Refresher(onClickListener = teacherListItems::retry) }
                    }
                    teacherListItems.itemCount == 0 -> {
                        item { EmptyView() }
                    }
                }
            }
        }
    }
}

@Composable
fun Teacher(
    teacher: Teacher,
    onClick: ClickListener
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable(onClick = onClick)
    ) {
        Row {
            AsyncImage(
                model = teacher.avatar,
                contentDescription = "avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(70.dp)
            )
            SpacerWidth(width = 10.dp)
            Column {
                Text(
                    text = teacher.name,
                    style = MaterialTheme3.typography.titleMedium,
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                SpacerHeight(height = 3.dp)
                WithContentAlpha(alpha = ContentAlpha.medium) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = FluentIcons.ic_fluent_info_16_regular),
                            contentDescription = null,
                            modifier = Modifier
                                .size(17.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = teacher.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
//                            color = MaterialTheme3.colorScheme.secondary,
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
//    Card(shape = MaterialTheme.shapes.medium, elevation = 2.dp, modifier = Modifier
//        .fillMaxWidth()
//        .padding(2.dp)) {
//        ConstraintLayout(modifier = Modifier.padding(5.dp)) {
//            val (name, image, type, group) = createRefs()
//            Text(text = teacher.name,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier
//                    .constrainAs(name) {
//                        start.linkTo(image.end)
//                        end.linkTo(parent.end)
//                        width = Dimension.fillToConstraints
//                    }
//                    .padding(start = 10.dp))
//            teacher.stuffType?.let {
//                Text(text = it,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier
//                        .constrainAs(type) {
//                            start.linkTo(image.end)
//                            end.linkTo(parent.end)
//                            top.linkTo(name.bottom)
//                            width = Dimension.fillToConstraints
//                        }
//                        .padding(start = 10.dp))
//            }
//            Text(text = teacher.description,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier
//                    .constrainAs(group) {
//                        start.linkTo(image.end)
//                        end.linkTo(parent.end)
//                        top.linkTo(type.bottom)
//                        width = Dimension.fillToConstraints
//                    }
//                    .padding(start = 10.dp))
//
//            teacher.avatar?.let {
//                Image(
//                    painter = rememberImagePainter(
//                        data = it,
//                        builder = {
//                            transformations(CircleCropTransformation())
//                        }
//                    ),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(100.dp)
//                        .constrainAs(image) {
//                            start.linkTo(parent.start)
//                            top.linkTo(parent.top)
//                        }
//                )
//            }
//
//        }
//    }
}
