package io.edugma.features.account.teachers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.model.description
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.FluentIcons
import io.edugma.features.base.core.utils.MaterialTheme3
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
                    onValueChange = viewModel::setName)
                SpacerHeight(height = 40.dp)
                PrimaryButton(
                    onClick = {
                        viewModel.load(state.name)
                        scope.launch { bottomState.hide() }
                              },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Применить")
                }
                SpacerHeight(height = 15.dp)
            }
        }
    ) {
        TeachersContent(state,
            backListener = { viewModel.exit() },
            openBottomListener = { scope.launch {bottomState.show() } })
    }
}

@Composable
fun TeachersContent(
    state: TeachersState,
    openBottomListener: ClickListener,
    backListener: ClickListener
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
                        Teacher(teacher = it)
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
fun Teacher(teacher: Teacher) {
    Card(shape = MaterialTheme.shapes.medium, elevation = 2.dp, modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp)) {
        ConstraintLayout(modifier = Modifier.padding(5.dp)) {
            val (name, image, type, group) = createRefs()
            Text(text = teacher.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .constrainAs(name) {
                        start.linkTo(image.end)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .padding(start = 10.dp))
            teacher.stuffType?.let {
                Text(text = it,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .constrainAs(type) {
                            start.linkTo(image.end)
                            end.linkTo(parent.end)
                            top.linkTo(name.bottom)
                            width = Dimension.fillToConstraints
                        }
                        .padding(start = 10.dp))
            }
            Text(text = teacher.description,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .constrainAs(group) {
                        start.linkTo(image.end)
                        end.linkTo(parent.end)
                        top.linkTo(type.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .padding(start = 10.dp))

            teacher.avatar?.let {
                Image(
                    painter = rememberImagePainter(
                        data = it,
                        builder = {
                            transformations(CircleCropTransformation())
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .constrainAs(image) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        }
                )
            }

        }
    }
}
