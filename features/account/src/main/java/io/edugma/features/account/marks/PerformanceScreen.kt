package io.edugma.features.account.marks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.textField.EdTextField
import io.edugma.core.designSystem.organism.nothingFound.EdNothingFound
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.domain.account.model.Performance
import io.edugma.features.account.R
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.core.utils.format
import io.edugma.features.base.core.utils.isNull
import io.edugma.features.base.elements.Chip
import io.edugma.features.base.elements.ErrorView
import io.edugma.features.base.elements.SelectableChip
import io.edugma.features.base.elements.placeholder
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PerformanceScreen(viewModel: PerformanceViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()
    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val scope = rememberCoroutineScope()

    FeatureScreen {
        ModalBottomSheetLayout(
            sheetState = bottomState,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            scrimColor = Color.Black.copy(alpha = 0.5f),
            sheetBackgroundColor = EdTheme.colorScheme.surface,
            sheetContent = {
                BottomSheetContent(
                    state,
                    viewModel,
                ) { scope.launch { bottomState.hide() } }
            },
        ) {
            PerformanceContent(
                state,
                showBottomSheet = { scope.launch { bottomState.show() } },
                retryListener = viewModel::loadMarks,
                filterClickListener = viewModel::updateFilter,
                backListener = viewModel::exit,
            )
        }
    }
}

@Composable
fun BottomSheetContent(
    state: MarksState,
    viewModel: PerformanceViewModel,
    bottomCloseListener: ClickListener,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp),
    ) {
        SpacerHeight(height = 15.dp)
        Text(
            text = "Фильтры",
            style = EdTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 8.dp),
        )
        SpacerHeight(height = 20.dp)
        EdTextField(
            modifier = Modifier.placeholder(state.bottomSheetPlaceholders),
            value = state.name.value,
            placeholder = "Название предмета",
            onValueChange = {
                viewModel.updateFilter(
                    Filter.Name(
                        value = it,
                        isChecked = it.isEmpty(),
                    ),
                )
            },
        )
        SpacerHeight(height = 20.dp)
        SelectableChipsRow(
            "Курс",
            state.courses,
            viewModel::updateFilter,
            state.bottomSheetPlaceholders,
        )
        SpacerHeight(height = 20.dp)
        SelectableChipsRow(
            "Семестр",
            state.semesters,
            viewModel::updateFilter,
            state.bottomSheetPlaceholders,
        )
        SpacerHeight(height = 20.dp)
        SelectableChipsRow(
            "Тип",
            state.types,
            viewModel::updateFilter,
            state.bottomSheetPlaceholders,
        )
        SpacerHeight(height = 20.dp)
        EdButton(
            onClick = bottomCloseListener,
            modifier = Modifier
                .fillMaxWidth()
                .placeholder(state.bottomSheetPlaceholders),
            text = "Применить",
        )
        SpacerHeight(height = 15.dp)
    }
}

@Composable
fun PerformanceContent(
    state: MarksState,
    showBottomSheet: ClickListener,
    retryListener: ClickListener,
    filterClickListener: Typed1Listener<Filter<*>>,
    backListener: ClickListener,
) {
    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing), onRefresh = retryListener) {
        Column {
            AppBar(showBottomSheet = showBottomSheet, backListener = backListener)
            FiltersRow(state.currentFilters, filterClickListener)
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .fillMaxSize(),
            ) {
                when {
                    state.isError && state.data.isNull() -> {
                        item { ErrorView(retryAction = retryListener) }
                    }
                    state.placeholders -> {
                        items(3) {
                            SpacerHeight(height = 3.dp)
                            PerformancePlaceholder()
                            Divider()
                        }
                    }
                    state.filteredData?.isEmpty() == true -> {
                        item { EdNothingFound() }
                    }
                    else -> {
                        items(
                            count = state.filteredData?.size ?: 0,
                            key = { state.filteredData!![it].id },
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
                                    style = EdTheme.typography.headlineSmall,
                                    color = EdTheme.colorScheme.primary,
                                )
                                SpacerHeight(height = 15.dp)
                            }
                            Performance(
                                state.filteredData!![it],
                                filterClickListener,
                            )
                            SpacerHeight(height = 3.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppBar(
    showBottomSheet: ClickListener,
    backListener: ClickListener,
) {
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
                text = "Оценки",
                style = EdTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        IconButton(
            onClick = showBottomSheet,
            modifier = Modifier.constrainAs(filter) {
                linkTo(parent.top, parent.bottom)
                end.linkTo(parent.end)
            },
        ) {
            Icon(
                painterResource(id = EdIcons.ic_fluent_filter_24_regular),
                contentDescription = "Фильтр",
            )
        }
    }
}

@Composable
fun Performance(
    performance: Performance,
    filterClickListener: Typed1Listener<Filter<*>>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = performance.name,
            style = EdTheme.typography.titleMedium.copy(fontSize = 19.sp),
            modifier = Modifier.heightIn(30.dp),
        )
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (data, mark) = createRefs()
            Column(
                modifier = Modifier.constrainAs(data) {
                    linkTo(parent.top, parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(mark.start)
                    width = Dimension.fillToConstraints
                },
            ) {
                EdLabel(
                    text = performance.teacher,
                    iconPainter = painterResource(id = R.drawable.acc_ic_teacher_24),
                )
                performance.date?.let {
                    EdLabel(
                        text = "${performance.date?.format()} ${performance.time?.format().orEmpty()}",
                        iconPainter = painterResource(id = EdIcons.ic_fluent_calendar_ltr_24_regular),
                    )
                }
            }
            Text(
                text = performance.grade,
                style = EdTheme.typography.titleLarge,
                modifier = Modifier.constrainAs(mark) {
                    linkTo(parent.top, parent.bottom)
                    end.linkTo(parent.end)
                },
            )
        }
        Row {
            Chip(
                modifier = Modifier.clickable(onClick = {
                    filterClickListener.invoke(
                        Filter.Course(
                            performance.course,
                        ),
                    )
                },),
            ) {
                Text(
                    text = "${performance.course} курс",
                    style = EdTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Chip(
                modifier = Modifier.clickable(onClick = {
                    filterClickListener.invoke(
                        Filter.Semester(
                            performance.semester,
                        ),
                    )
                },),
            ) {
                Text(
                    text = "${performance.semester} семестр",
                    style = EdTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Chip(
                modifier = Modifier.clickable(onClick = {
                    filterClickListener.invoke(
                        Filter.Type(
                            performance.examType,
                        ),
                    )
                },),
            ) {
                Text(
                    text = performance.examType,
                    style = EdTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
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
            style = EdTheme.typography.titleMedium.copy(fontSize = 19.sp),
            modifier = Modifier
                .widthIn(min = 200.dp)
                .placeholder(true),
        )
        SpacerHeight(height = 10.dp)
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (data, mark) = createRefs()
            Column(
                modifier = Modifier.constrainAs(data) {
                    linkTo(parent.top, parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(mark.start)
                    width = Dimension.fillToConstraints
                },
            ) {
                EdLabel(
                    text = "",
                    iconPainter = painterResource(id = R.drawable.acc_ic_teacher_24),
                    modifier = Modifier.placeholder(true),
                )
                EdLabel(
                    text = "",
                    iconPainter = painterResource(id = EdIcons.ic_fluent_calendar_ltr_24_regular),
                    modifier = Modifier.placeholder(true),
                )
            }
            Text(
                text = "",
                style = EdTheme.typography.titleLarge,
                modifier = Modifier
                    .widthIn(min = 100.dp)
                    .placeholder(true)
                    .constrainAs(mark) {
                        linkTo(parent.top, parent.bottom)
                        end.linkTo(parent.end)
                    },
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
private fun<T> SelectableChipsRow(
    name: String,
    items: Set<Filter<T>>,
    onClick: Typed1Listener<Filter<T>>,
    placeholders: Boolean,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = name,
            style = EdTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        SpacerWidth(width = 10.dp)
        LazyRow {
            if (placeholders) {
                items(4) {
                    Chip(Modifier.placeholder(true)) {}
                }
            } else {
                val listItems = items.toList()
                items(
                    count = listItems.size,
                    key = { listItems[it].hashCode() },
                ) {
                    SelectableChip(
                        selectedState = listItems[it].isChecked,
                        onClick = { onClick.invoke(listItems[it]) },
                    ) {
                        Text(
                            text = listItems[it].mappedValue,
                            style = EdTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FiltersRow(
    filters: Set<Filter<*>>,
    filterClickListener: Typed1Listener<Filter<*>>,
) {
    LazyRow {
        val filtersList = filters.toList()
        items(
            count = filtersList.size,
            key = { filtersList[it].hashCode() },
        ) {
            Chip(
                icon = Icons.Rounded.Close,
                onClick = { filterClickListener.invoke(filtersList[it]) },
            ) {
                Text(
                    text = filtersList[it].mappedValue,
                    style = EdTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
