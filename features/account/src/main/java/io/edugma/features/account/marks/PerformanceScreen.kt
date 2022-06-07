package io.edugma.features.account.marks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import io.edugma.features.account.marks.Filter.*
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
            Column(modifier = Modifier
                .padding(horizontal = 15.dp)) {
                SpacerHeight(height = 15.dp)
                Text(
                    text = "Фильтры",
                    style = MaterialTheme3.typography.headlineMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
                SpacerHeight(height = 20.dp)
                TextBox(
                    value = state.name.value,
                    title = "Название предмета",
                    onValueChange = {
                        viewModel.updateFilter(
                            Name(
                                value = it,
                                isChecked = it.isEmpty()
                            )
                        )
                    })
                SpacerHeight(height = 20.dp)
                ChipsRow(
                    "Курс",
                    state.courses,
                    viewModel::updateFilter,
                    state.placeholders
                )
                SpacerHeight(height = 20.dp)
                ChipsRow(
                    "Семестр",
                    state.semesters,
                    viewModel::updateFilter,
                    state.placeholders
                )
                SpacerHeight(height = 20.dp)
                ChipsRow(
                    "Тип",
                    state.types,
                    viewModel::updateFilter,
                    state.placeholders
                )
                SpacerHeight(height = 20.dp)
                PrimaryButton(
                    onClick = {scope.launch { bottomState.hide() }},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Применить")
                }
                SpacerHeight(height = 15.dp)
            }
        }
    ) {
        PerformanceContent(
            state,
            showBottomSheet = {scope.launch { bottomState.show() }},
            retryListener = viewModel::loadMarks,
            filterClickListener = viewModel::updateFilter,
            backListener = viewModel::exit
        )
    }
}

@Composable
fun PerformanceContent(state: MarksState,
                       showBottomSheet: ClickListener,
                       retryListener: ClickListener,
                       filterClickListener: Typed1Listener<Filter<*>>,
                       backListener: ClickListener) {
    Column {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (content, filter) = createRefs()
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.constrainAs(content) {
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
                    text = "Оценки",
                    style = MaterialTheme3.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(onClick = showBottomSheet, modifier = Modifier.constrainAs(filter) {
                linkTo(parent.top, parent.bottom)
                end.linkTo(parent.end)
            }) {
                Icon(
                    painterResource(id = FluentIcons.ic_fluent_filter_24_regular),
                    contentDescription = "Фильтр"
                )
            }
        }
        FiltersRow(
            state.currentFilters,
            filterClickListener
        )
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            when {
                state.isError -> {
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
                        count = state.filteredData?.size ?: 0,
                        key = { state.filteredData!![it].id }
                    ) {
                        var showCourse = true
                        if (it > 0) {
                            showCourse = state.filteredData!![it].course != state.filteredData[it - 1].course
                        }
                        if (!showCourse && it > 0) {
                            Divider()
                            SpacerHeight(height = 3.dp)
                        }
                        if (showCourse) {
                            if (it > 0) SpacerHeight(height = 15.dp)
                            Text(
                                text = "${state.filteredData!![it].course} курс",
                                style = MaterialTheme3.typography.headlineSmall,
                                color = MaterialTheme3.colorScheme.primary
                            )
                            SpacerHeight(height = 15.dp)
                        }
                        Performance(
                            state.filteredData!![it],
                            filterClickListener
                        )
                        SpacerHeight(height = 3.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun Performance(
    performance: Performance,
    filterClickListener: Typed1Listener<Filter<*>>,
) {
    Column(modifier = Modifier
        .fillMaxWidth()) {
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
            Chip(modifier = Modifier.clickable(onClick = {filterClickListener.invoke(Course(performance.course))})) {
                Text(
                    text = "${performance.course} курс",
                    style = MaterialTheme3.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Chip(modifier = Modifier.clickable(onClick = {filterClickListener.invoke(Semester(performance.semester))})) {
                Text(
                    text = "${performance.semester} семестр",
                    style = MaterialTheme3.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Chip(modifier = Modifier.clickable(onClick = {filterClickListener.invoke(Type(performance.examType))})) {
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
            modifier = Modifier
                .widthIn(min = 200.dp)
                .placeholder(true)
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
            Chip(modifier = Modifier.placeholder(true))
            Chip(modifier = Modifier.placeholder(true))
            Chip(modifier = Modifier.placeholder(true))
        }
    }
}

@Composable
private fun<T> ChipsRow(
    name: String,
    items: Set<Filter<T>>,
    onClick: Typed1Listener<Filter<T>>,
    placeholders: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = name,
            style = MaterialTheme3.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis)
        SpacerWidth(width = 10.dp)
        LazyRow {
            if (placeholders) {
                items(4) {
                    Chip(Modifier.placeholder(true)) {}
                }
            }
            val listItems = items.toList()
            items(
                count = listItems.size,
                key = { listItems[it].hashCode() }
            ) {
                SelectableChip(
                    selectedState = listItems[it].isChecked,
                    onClick = { onClick.invoke(listItems[it]) }) {
                    Text(
                        text = listItems[it].mappedValue,
                        style = MaterialTheme3.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun FiltersRow(
    filters: Set<Filter<*>>,
    filterClickListener: Typed1Listener<Filter<*>>
) {
    LazyRow {
        val filtersList = filters.toList()
        items(
            count = filtersList.size,
            key = {filtersList[it].hashCode()}
        ) {
            Chip(
                icon = Icons.Rounded.Close,
                onClick = { filterClickListener.invoke(filtersList[it]) }) {
                Text(
                    text = filtersList[it].mappedValue,
                    style = MaterialTheme3.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

