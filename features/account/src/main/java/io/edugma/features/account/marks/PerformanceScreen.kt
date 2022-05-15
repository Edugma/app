package io.edugma.features.account.marks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.edugma.domain.account.model.Performance
import io.edugma.features.account.R
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.*
import org.koin.androidx.compose.getViewModel

@Composable
fun PerformanceScreen(viewModel: PerformanceViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    MarksContent(state,
        retryListener = { viewModel.loadMarks() },
        backListener = { viewModel.exit() },
        inputListener = {  }
    )
}

@Composable
fun MarksContent(state: MarksState,
                 retryListener: ClickListener,
                 backListener: ClickListener,
                 inputListener: Typed1Listener<Int>) {
    Column {
        var expandedState by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(if (state.coursesAndSemesters.keys.isNotEmpty()) state.coursesAndSemesters.keys.first() else 1) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { backListener.invoke() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
        }
        Text(text = "Оценки")
        //            DropdownMenu(expanded = expandedState, onDismissRequest = { expandedState = false }) {
//            Text("${state.coursesAndSemesters[selectedIndex]} курс $selectedIndex семестр",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable(onClick = { expandedState = true }))
//            state.coursesAndSemesters.forEach {
//                DropdownMenuItem(onClick = {
//                    selectedIndex = it.key
//                    expandedState = false
//                }) {
//                    Text(text = "${it.value} курс ${it.key} семестр")
//                }
//            }
//        }
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            if (state.isError && state.data.isEmpty()) {
                item {
                    ErrorView {
                        retryListener.invoke()
                    }
                }
            } else {
                items(
                    count = state.data.size,
                    key = { state.data[it].id }
                ) {
                    SpacerHeight(height = 3.dp)
                    Performance(state.data[it])
                    Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Performance(performance: Performance) {
    Column(modifier = Modifier.fillMaxWidth()) {
//        ConstraintLayout(modifier = Modifier
//            .padding(5.dp)
//            .fillMaxWidth()
//            .heightIn(70.dp)) {
//            val (data, addtitonalData, mark) = createRefs()
//            Column(modifier = Modifier.constrainAs(data) {
//                start.linkTo(parent.start)
//                top.linkTo(parent.top)
//                end.linkTo(mark.start)
//                width = Dimension.fillToConstraints
//            }) {
//                Text(
//                    text = performance.name,
//                    style = MaterialTheme3.typography.titleMedium.copy(fontSize = 19.sp)
//                )
//                SpacerHeight(height = 10.dp)
//                Text(text = performance.examType)
//                Text(text = performance.teacher)
//                Text(text = "${performance.course} курс")
//                performance.date?.let {
//                    Text(text = performance.date!!.format())
//                }
//
//            }
//            Text(
//                text = performance.grade,
//                style = MaterialTheme3.typography.labelLarge,
//                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier.constrainAs(mark) {
//                    end.linkTo(parent.end)
//                    linkTo(top = parent.top, bottom = parent.bottom)
//                })
//        }
        Text(
            text = performance.name,
            style = MaterialTheme3.typography.titleMedium.copy(fontSize = 19.sp)
        )
        SpacerHeight(height = 10.dp)
        TextWithIcon(
            text = performance.teacher,
            icon = painterResource(id = R.drawable.acc_ic_teacher_24)
        )
        TextWithIcon(
            text = performance.examType,
            icon = painterResource(id = FluentIcons.ic_fluent_book_24_regular)
        )
        performance.date?.let {
            TextWithIcon(
                text = performance.date?.format(),
                icon = painterResource(id = FluentIcons.ic_fluent_calendar_ltr_24_regular)
            )
        }

        Row {
            Chip {
                Text(
                    text = performance.grade,
                    style = MaterialTheme3.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis
                )
            }
            SpacerWidth(width = 5.dp)
            Chip {
                Text(
                    text = "${performance.course} курс",
                    style = MaterialTheme3.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
