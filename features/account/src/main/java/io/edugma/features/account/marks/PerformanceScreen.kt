package io.edugma.features.account.marks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import io.edugma.domain.account.model.Performance
import io.edugma.features.account.R
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PerformanceScreen(viewModel: PerformanceViewModel = getViewModel()) {
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
            LazyColumn {
                items(50) {
                    ListItem(
                        text = { Text("Item $it") },
                        icon = {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Localized description"
                            )
                        }
                    )
                }
            }
        }
    ) {
        PerformanceContent(
            state,
            showBottomSheet = {scope.launch { bottomState.show() }},
            retryListener = { viewModel.loadMarks() },
            backListener = { viewModel.exit() }
        )
    }
}

@Composable
fun PerformanceContent(state: MarksState,
                       showBottomSheet: ClickListener,
                       retryListener: ClickListener,
                       backListener: ClickListener) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { backListener.invoke() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
            SpacerWidth(width = 15.dp)
            Text(
                text = "Оценки",
                style = MaterialTheme3.typography.titleLarge
            )
        }
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            when {
                state.isError && state.data.isEmpty() -> {
                    item {
                        ErrorView {
                            retryListener.invoke()
                        }
                    }
                }
                state.placeholders -> {
                    items(3) {
                        SpacerHeight(height = 3.dp)
                        PerformancePlaceholder()
                        Divider()
                    }
                }
                else -> {
                    items(
                        count = state.data.size,
                        key = { state.data[it].id }
                    ) {
                        SpacerHeight(height = 3.dp)
                        Performance(state.data[it], showBottomSheet)
                        SpacerHeight(height = 3.dp)
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun Performance(performance: Performance, showBottomSheet: ClickListener) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = performance.name,
            style = MaterialTheme3.typography.titleMedium.copy(fontSize = 19.sp),
            modifier = Modifier.heightIn(30.dp)
        )
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (data, mark) = createRefs()
            Column(modifier = Modifier.constrainAs(data) {
                linkTo(parent.top, parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(mark.start)
                width = Dimension.fillToConstraints
            }) {
                TextWithIcon(
                    text = performance.teacher,
                    icon = painterResource(id = R.drawable.acc_ic_teacher_24)
                )
                performance.date?.let {
                    TextWithIcon(
                        text = "${performance.date?.format()} ${performance.time?.format().orEmpty()}",
                        icon = painterResource(id = FluentIcons.ic_fluent_calendar_ltr_24_regular)
                    )
                }
            }
            Text(
                text = performance.grade,
                style = MaterialTheme3.typography.titleLarge,
                modifier = Modifier.constrainAs(mark) {
                    linkTo(parent.top, parent.bottom)
                    end.linkTo(parent.end)
                }
            )
        }
        Row {
            Chip(modifier = Modifier.clickable(onClick = showBottomSheet)) {
                Text(
                    text = "${performance.course} курс",
                    style = MaterialTheme3.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Chip(modifier = Modifier.clickable(onClick = showBottomSheet)) {
                Text(
                    text = "${performance.semester} семестр",
                    style = MaterialTheme3.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Chip(modifier = Modifier.clickable(onClick = showBottomSheet)) {
                Text(
                    text = performance.examType,
                    style = MaterialTheme3.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun PerformancePlaceholder() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "",
            style = MaterialTheme3.typography.titleMedium.copy(fontSize = 19.sp),
            modifier = Modifier.widthIn(min = 200.dp).placeholder(true)
        )
        SpacerHeight(height = 10.dp)
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (data, mark) = createRefs()
            Column(modifier = Modifier.constrainAs(data) {
                linkTo(parent.top, parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(mark.start)
                width = Dimension.fillToConstraints
            }) {
                TextWithIcon(
                    text = "",
                    icon = painterResource(id = R.drawable.acc_ic_teacher_24),
                    modifier = Modifier.placeholder(true)
                )
                TextWithIcon(
                    text = "",
                    icon = painterResource(id = FluentIcons.ic_fluent_calendar_ltr_24_regular),
                    modifier = Modifier.placeholder(true)
                )
            }
            Text(
                text = "",
                style = MaterialTheme3.typography.titleLarge,
                modifier = Modifier
                    .widthIn(min = 100.dp)
                    .placeholder(true)
                    .constrainAs(mark) {
                        linkTo(parent.top, parent.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }
        Row {
            Chip {
//                Text(
//                    text = "",
//                    style = MaterialTheme3.typography.labelLarge,
//                    overflow = TextOverflow.Ellipsis,
//                    maxLines = 1,
//                    modifier = Modifier.placeholder(true)
//                )
            }
            Chip {
//                Text(
//                    text = "",
//                    style = MaterialTheme3.typography.labelLarge,
//                    overflow = TextOverflow.Ellipsis,
//                    maxLines = 1,
//                    modifier = Modifier.placeholder(true).
//                )
            }
            Chip {
//                Text(
//                    text = "",
//                    style = MaterialTheme3.typography.labelLarge,
//                    overflow = TextOverflow.Ellipsis,
//                    maxLines = 1,
//                    modifier = Modifier.placeholder(true)
//                )
            }
        }
    }
}
