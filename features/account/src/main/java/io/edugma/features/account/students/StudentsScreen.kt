package io.edugma.features.account.students

import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.edugma.domain.account.model.student.Student
import io.edugma.domain.account.model.print
import io.edugma.features.account.R
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudentsScreen(viewModel: StudentsViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        scrimColor = Color.Black.copy(alpha = 0.5f),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContent = {
            when (state.bottomType) {
                BottomSheetType.Filter -> {
                    FilterSheetContent(state = state, textChangeListener = viewModel::setName) {
                        viewModel.load(state.name)
                        scope.launch { bottomState.hide() }
                    }
                }
                BottomSheetType.Student -> {
                    state.selectedStudent?.let { StudentSheetContent(it) }
                }
            }
            
        }
    ) {
        StudentsContent(
            state,
            backListener = viewModel::exit,
            openBottomSheetListener = {
                viewModel.selectFilter()
                scope.launch { bottomState.show() }
            },
            studentClick = {
                viewModel.selectStudent(it)
                scope.launch { bottomState.show() }
            }
        )
    }
}

@Composable
fun StudentSheetContent(
    student: Student
) {
    Text(text = student.toString())
}

@Composable
fun FilterSheetContent(
    state: StudentsState,
    textChangeListener: Typed1Listener<String>,
    onAcceptClick: ClickListener
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
            title = "Фамилия или группа студента",
            onValueChange = textChangeListener)
        SpacerHeight(height = 40.dp)
        PrimaryButton(
            onClick = onAcceptClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Применить")
        }
        SpacerHeight(height = 15.dp)
    }
}

@Composable
fun StudentsContent(
    state: StudentsState,
    backListener: ClickListener,
    openBottomSheetListener: ClickListener,
    studentClick: Typed1Listener<Student>
) {
    val studentListItems = state.pagingData?.collectAsLazyPagingItems()
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
                    text = "Студенты",
                    style = MaterialTheme3.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(modifier = Modifier.constrainAs(filter) {
                linkTo(parent.top, parent.bottom)
                end.linkTo(parent.end)
            }) {
                val students = studentListItems
                    ?.itemSnapshotList
                    ?.items
                    ?.mapIndexed { index, student -> "${index + 1}. ${student.getFullName()}" }
                if (students?.isNotEmpty() == true) {
                    val context = LocalContext.current
                    IconButton(onClick = {
                        Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, students.joinToString("\n"))
                            type = "text/plain"
                        }
                            .let { Intent.createChooser(it, null) }
                            .also(context::startActivity)
                    }
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Поделиться"
                        )
                    }
                }
                IconButton(onClick = openBottomSheetListener) {
                    Icon(
                        painterResource(id = FluentIcons.ic_fluent_search_24_regular),
                        contentDescription = "Фильтр"
                    )
                }
            }
        }
        LazyColumn(Modifier.fillMaxSize()) {
            studentListItems?.let { _ ->
                items(studentListItems) { item ->
                    item?.let {
                        Student(it) { studentClick.invoke(it) }
                    }
                }
                when {
                    studentListItems.loadState.refresh is LoadState.Loading -> {
                        item { Text(text = "placeholders") }
                    }
                    studentListItems.loadState.refresh is LoadState.Error -> {
                        item { ErrorView(retryAction = studentListItems::refresh) }
                    }
                    studentListItems.loadState.append is LoadState.Loading -> {
                        item {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 70.dp)) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                    studentListItems.loadState.append is LoadState.Error -> {
                        item { Refresher(onClickListener = studentListItems::retry) }
                    }
                    studentListItems.itemCount == 0 -> {
                        item { EmptyView() }
                    }
                }
            }
        }
    }
}

@Composable
fun Student(student: Student, onClick: ClickListener) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(onClick = onClick)
    ) {
        Row {
            AsyncImage(
                model = student.avatar,
                contentDescription = "avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp)
            )
            SpacerWidth(width = 10.dp)
            Column {
                Text(
                    text = student.getFullName(),
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
                            text = student.getInfo(),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
//                            color = MaterialTheme3.colorScheme.secondary,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    }
                }
            }
        }
        Divider(modifier = Modifier.padding(start = 65.dp, top = 2.dp))
    }
}
