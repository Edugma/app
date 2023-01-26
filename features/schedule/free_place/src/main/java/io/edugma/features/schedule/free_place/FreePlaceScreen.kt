package io.edugma.features.schedule.free_place

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.domain.schedule.model.place.description
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.*
import io.edugma.features.base.elements.dialogs.core.MaterialDialog
import io.edugma.features.base.elements.dialogs.core.rememberMaterialDialogState
import io.edugma.features.base.elements.dialogs.date.datepicker
import io.edugma.features.base.elements.dialogs.time.timepicker
import org.koin.androidx.compose.getViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun FreePlaceScreen(viewModel: FreePlaceViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    FreePlaceContent(
        state,
        viewModel::exit,
        viewModel::onDateSelect,
        viewModel::onTimeFromSelect,
        viewModel::onTimeToSelect,
        viewModel::onEnterFilterQuery,
        viewModel::onFindFreePlaces,
        onShowFilters = viewModel::onShowFilters,
    )
}

private val dateFormat = DateTimeFormatter.ofPattern("d MMMM yyyy")
private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreePlaceContent(
    state: FreePlaceState,
    onBackClick: ClickListener,
    onDateSelect: (LocalDate) -> Unit,
    onTimeFromSelect: (LocalTime) -> Unit,
    onTimeToSelect: (LocalTime) -> Unit,
    onEnterFilterQuery: (String) -> Unit,
    onFindFreePlaces: ClickListener,
    onShowFilters: ClickListener,
) {
    Box(
        Modifier
            .fillMaxSize(),
    ) {
        val dialogDatePickerState = rememberMaterialDialogState()
        val dialogTimePickerFromState = rememberMaterialDialogState()
        val dialogTimePickerToState = rememberMaterialDialogState()

        Column(Modifier.fillMaxSize()) {
            EdTopAppBar(
                title = stringResource(R.string.schedule_fre_pla_find_free_place),
                onNavigationClick = onBackClick,
            )
            Column(Modifier.padding(start = 8.dp, end = 8.dp)) {
                TonalCard(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 16.dp,
                            ),
                    ) {
                        Text(
                            text = "Фильтры",
                            modifier = Modifier.clickable { onShowFilters() },
                            style = MaterialTheme3.typography.titleLarge,
                        )
                        SpacerHeight(height = 12.dp)
                        if (state.showFilters) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Дата",
                                    style = MaterialTheme3.typography.titleMedium,
                                )
                                SpacerFill()
                                EdButton(
                                    onClick = { dialogDatePickerState.show() },
                                    text = state.date.format(dateFormat),
                                )
                            }
                            SpacerHeight(4.dp)
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Начальное время",
                                    style = MaterialTheme3.typography.titleMedium,
                                )
                                SpacerFill()
                                EdButton(
                                    onClick = { dialogTimePickerFromState.show() },
                                    text = state.timeFrom.format(timeFormat),
                                )
                            }
                            SpacerHeight(4.dp)
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Конечное время",
                                    style = MaterialTheme3.typography.titleMedium,
                                )
                                SpacerFill()
                                EdButton(
                                    onClick = { dialogTimePickerToState.show() },
                                    text = state.timeTo.format(timeFormat),
                                )
                            }
                            SpacerHeight(8.dp)
                            OutlinedTextField(
                                value = state.filterQuery,
                                onValueChange = onEnterFilterQuery,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text = "Введите фильтр")
                                },
                            )
                            LazyColumn(
                                Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                            ) {
                                items(state.filteredPlaces) { item ->
                                    var checked by remember { mutableStateOf(false) }
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable { checked = !checked },
                                    ) {
                                        Text(
                                            text = item.title,
                                            modifier = Modifier
                                                .weight(weight = 1f, fill = true)
                                                .padding(horizontal = 10.dp, vertical = 10.dp),

                                        )

                                        Checkbox(
                                            checked = checked,
                                            onCheckedChange = { checked = it },
                                        )
                                    }
                                }
                            }
                            EdButton(
                                onClick = onFindFreePlaces,
                                text = "Найти свободные аудитории",
                            )
                        }
                    }
                    SpacerHeight(height = 10.dp)
                    val freePlaces = remember(state.freePlaces) {
                        state.freePlaces.toList().sortedBy { it.first.title }
                    }
                    LazyColumn(
                        Modifier.fillMaxSize(),
                    ) {
                        items(freePlaces) { item ->
                            var checked by remember { mutableStateOf(false) }
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { checked = !checked },
                            ) {
                                Text(
                                    text = item.first.title,
                                    style = MaterialTheme3.typography.titleSmall,
                                )
                                MediumAlpha {
                                    Text(
                                        text = item.first.description,
                                        style = MaterialTheme3.typography.titleSmall,
                                    )
                                }
                                WithContentAlpha(alpha = ContentAlpha.medium) {
                                    Text(
                                        text = "Занятий в это время: " + item.second.toString(),
                                        style = MaterialTheme3.typography.bodySmall,
                                    )
                                }
                                Spacer(Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }

            MaterialDialog(
                dialogState = dialogDatePickerState,
                buttons = {
                    positiveButton("Ок")
                    negativeButton("Отмена")
                },
            ) {
                datepicker(title = "Выберите день") { date ->
                    onDateSelect(date)
                }
            }

            MaterialDialog(
                dialogState = dialogTimePickerFromState,
                buttons = {
                    positiveButton("Ок")
                    negativeButton("Отмена")
                },
            ) {
                timepicker(title = "Выберите начальное время", is24HourClock = true) { time ->
                    onTimeFromSelect(time)
                }
            }

            MaterialDialog(
                dialogState = dialogTimePickerToState,
                buttons = {
                    positiveButton("Ок")
                    negativeButton("Отмена")
                },
            ) {
                timepicker(title = "Выбрать конечное время", is24HourClock = true) { time ->
                    onTimeToSelect(time)
                }
            }
        }
    }
}
