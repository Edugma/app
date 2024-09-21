package com.edugma.features.schedule.freePlace

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edugma.core.api.utils.DateFormat
import com.edugma.core.api.utils.TimeFormat
import com.edugma.core.api.utils.format
import com.edugma.core.designSystem.atoms.card.EdCard
import com.edugma.core.designSystem.atoms.spacer.SpacerFill
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.molecules.button.EdButton
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.utils.SecondaryContent
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.ClickListener
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel2
import edugma.shared.core.icons.generated.resources.*
import edugma.shared.core.resources.generated.resources.Res
import edugma.shared.core.resources.generated.resources.schedule_fre_pla_find_free_place
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource

@Composable
fun FreePlaceScreen(viewModel: FreePlaceViewModel = getViewModel2()) {
    val state by viewModel.collectAsState()

    FeatureScreen {
        FreePlaceContent(
            state = state,
            onBackClick = viewModel::exit,
            onDateSelect = viewModel::onDateSelect,
            onTimeFromSelect = viewModel::onTimeFromSelect,
            onTimeToSelect = viewModel::onTimeToSelect,
            onEnterFilterQuery = viewModel::onEnterFilterQuery,
            onFindFreePlaces = viewModel::onFindFreePlaces,
            onShowFilters = viewModel::onShowFilters,
        )
    }
}

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
//        val dialogDatePickerState = rememberMaterialDialogState()
//        val dialogTimePickerFromState = rememberMaterialDialogState()
//        val dialogTimePickerToState = rememberMaterialDialogState()

        Column(Modifier.fillMaxSize()) {
            EdTopAppBar(
                title = stringResource(Res.string.schedule_fre_pla_find_free_place),
                onNavigationClick = onBackClick,
            )
            Column(Modifier.padding(start = 8.dp, end = 8.dp)) {
                EdCard(Modifier.fillMaxWidth()) {
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
                            style = EdTheme.typography.titleLarge,
                        )
                        SpacerHeight(height = 12.dp)
                        if (state.showFilters) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Дата",
                                    style = EdTheme.typography.titleMedium,
                                )
                                SpacerFill()
                                EdButton(
                                    onClick = { },
                                    text = state.date.format(DateFormat.FULL),
                                )
                            }
                            SpacerHeight(4.dp)
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Начальное время",
                                    style = EdTheme.typography.titleMedium,
                                )
                                SpacerFill()
                                EdButton(
                                    onClick = { },
                                    text = state.timeFrom.format(TimeFormat.HOURS_MINUTES),
                                )
                            }
                            SpacerHeight(4.dp)
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Конечное время",
                                    style = EdTheme.typography.titleMedium,
                                )
                                SpacerFill()
                                EdButton(
                                    onClick = { },
                                    text = state.timeTo.format(TimeFormat.HOURS_MINUTES),
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
                                    style = EdTheme.typography.titleSmall,
                                )
                                SecondaryContent {
                                    Text(
                                        text = item.first.description.orEmpty(),
                                        style = EdTheme.typography.titleSmall,
                                    )
                                }
                                SecondaryContent {
                                    Text(
                                        text = "Занятий в это время: " + item.second.toString(),
                                        style = EdTheme.typography.bodySmall,
                                    )
                                }
                                SpacerHeight(height = 10.dp)
                            }
                        }
                    }
                }
            }

//            MaterialDialog(
//                dialogState = dialogDatePickerState,
//                buttons = {
//                    positiveButton("Ок")
//                    negativeButton("Отмена")
//                },
//            ) {
//                datepicker(title = "Выберите день") { date ->
//                    onDateSelect(date)
//                }
//            }
//
//            MaterialDialog(
//                dialogState = dialogTimePickerFromState,
//                buttons = {
//                    positiveButton("Ок")
//                    negativeButton("Отмена")
//                },
//            ) {
//                timepicker(title = "Выберите начальное время", is24HourClock = true) { time ->
//                    onTimeFromSelect(time)
//                }
//            }
//
//            MaterialDialog(
//                dialogState = dialogTimePickerToState,
//                buttons = {
//                    positiveButton("Ок")
//                    negativeButton("Отмена")
//                },
//            ) {
//                timepicker(title = "Выбрать конечное время", is24HourClock = true) { time ->
//                    onTimeToSelect(time)
//                }
//            }
        }
    }
}
