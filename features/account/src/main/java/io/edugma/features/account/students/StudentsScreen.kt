package io.edugma.features.account.students

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import io.edugma.domain.account.model.Student
import io.edugma.domain.account.model.print
import io.edugma.features.account.R
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.FluentIcons
import io.edugma.features.base.core.utils.MaterialTheme3
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
        StudentsContent(
            state,
            backListener = { viewModel.exit() },
            openBottomSheetListener = {scope.launch { bottomState.show() }}
        )
    }
}

@Composable
fun StudentsContent(
    state: StudentsState,
    backListener: ClickListener,
    openBottomSheetListener: ClickListener
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
                   ?.mapIndexed { index, student -> "${index + 1}. ${student.getFullName()}"}
                if (students?.isNotEmpty() == true) {
                    val context = LocalContext.current
                    IconButton(onClick = {
                        Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, students.joinToString("\n"))
                            type = "text/plain"
                        }
                            .let { Intent.createChooser(it, null) }
                            .also(context::startActivity) }
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
        LazyColumn {
            studentListItems?.let { _ ->
                items(studentListItems) { item ->
                    item?.let {
                        Student(it)
                    }
                }
                when {
                    studentListItems.loadState.refresh is LoadState.Loading -> {
                        item { Text(text = "first loading") }
                        //You can add modifier to manage load state when first time response page is loading
                    }
                    studentListItems.loadState.refresh is LoadState.Error -> {
                        item { Text(text = "error") }
                        //You can use modifier to show error message
                    }
                    studentListItems.loadState.append is LoadState.Loading -> {
                        item { Text(text = "next page loading") }
                        //You can add modifier to manage load state when next response page is loading
                    }
                    studentListItems.loadState.append is LoadState.Error -> {
                        item { Text(text = "error") }
                        //You can use modifier to show error message
                    }
                }
            }
        }
    }
}

@Composable
fun Student(student: Student?, placeholders: Boolean = false) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    ConstraintLayout(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()
        .clickable { isExpanded = !isExpanded }
    ) {
        val (name, image, type, divider) = createRefs()
        Text(text = student?.getFullName().orEmpty(),
            style = MaterialTheme3.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .constrainAs(name) {
                    start.linkTo(image.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(start = 10.dp)
                .placeholder(placeholders)
        )
        Column(modifier = Modifier
            .padding(start = 10.dp)
            .constrainAs(type) {
                linkTo(name.bottom, divider.top, bias = 0f)
                linkTo(name.start, parent.end)
                width = Dimension.fillToConstraints
            }) {
            if (placeholders) {
                TextWithIcon(
                    text = "",
                    icon = painterResource(id = R.drawable.acc_ic_teacher_24),
                    modifier = Modifier.placeholder(true)
                )
            } else {
                student?.educationType?.print()?.let {
                    TextWithIcon(
                        text = it,
                        icon = painterResource(id = R.drawable.acc_ic_teacher_24)
                    )
                }
                if (isExpanded) {
                    student?.specialization?.let {
                        TextWithIcon(text = it, icon = painterResource(id = FluentIcons.ic_fluent_book_24_regular))
                    }
                    student?.direction?.let {
                        TextWithIcon(text = it, icon = painterResource(id = FluentIcons.ic_fluent_book_open_24_regular))
                    }
                    student?.group?.let {
                        TextWithIcon(text = it, icon = painterResource(id = FluentIcons.ic_fluent_people_24_regular))
                    }
                }
            }
        }
        Image(
            painter = rememberImagePainter(
                data = student?.avatar,
                builder = {
                    transformations(CircleCropTransformation())
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    bottom.linkTo(divider.top)
                    linkTo(parent.top, divider.top, bias = 0f)
                }
                .placeholder(placeholders)
        )
        Divider(modifier = Modifier.constrainAs(divider) {
            start.linkTo(image.end)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
            width = Dimension.fillToConstraints
        })
    }
}
