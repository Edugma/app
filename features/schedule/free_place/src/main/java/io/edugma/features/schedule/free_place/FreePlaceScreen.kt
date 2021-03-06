package io.edugma.features.schedule.free_place

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.ContentAlpha
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.core.utils.WithContentAlpha
import io.edugma.features.base.elements.PrimaryButton
import io.edugma.features.base.elements.PrimaryTopAppBar
import io.edugma.features.base.elements.SpacerFill
import io.edugma.features.base.elements.SpacerHeight
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
        viewModel::onFindFreePlaces
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
    onFindFreePlaces: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
    ) {
        val dialogDatePickerState = rememberMaterialDialogState()
        val dialogTimePickerFromState = rememberMaterialDialogState()
        val dialogTimePickerToState = rememberMaterialDialogState()

        Column(Modifier.verticalScroll(rememberScrollState())) {
            PrimaryTopAppBar(
                title = stringResource(R.string.schedule_fre_pla_find_free_place),
                onBackClick = onBackClick
            )
            Column(Modifier.padding(start = 16.dp, end = 16.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "????????",
                        style = MaterialTheme3.typography.titleMedium
                    )
                    SpacerFill()
                    PrimaryButton(onClick = { dialogDatePickerState.show() }) {
                        Text(text = state.date.format(dateFormat))
                    }
                }
                SpacerHeight(4.dp)
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "?????????????????? ??????????",
                        style = MaterialTheme3.typography.titleMedium
                    )
                    SpacerFill()
                    PrimaryButton(onClick = { dialogTimePickerFromState.show() }) {
                        Text(text = state.timeFrom.format(timeFormat))
                    }
                }
                SpacerHeight(4.dp)
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "???????????????? ??????????",
                        style = MaterialTheme3.typography.titleMedium
                    )
                    SpacerFill()
                    PrimaryButton(onClick = { dialogTimePickerToState.show() }) {
                        Text(text = state.timeTo.format(timeFormat))
                    }
                }
                SpacerHeight(4.dp)
                OutlinedTextField(
                    value = state.filterQuery,
                    onValueChange = onEnterFilterQuery
                )
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)) {
                    items(state.filteredPlaces) { item ->
                        var checked by remember { mutableStateOf(false) }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { checked = !checked }) {
                            Text(
                                text = item.title,
                                modifier = Modifier
                                    .weight(weight = 1f, fill = true)
                                    .padding(horizontal = 10.dp, vertical = 10.dp)

                            )

                            Checkbox(
                                checked = checked,
                                onCheckedChange = { checked = it }
                            )
                        }
                    }
                }
                PrimaryButton(onClick = onFindFreePlaces) {
                    Text(text = "?????????? ?????????????????? ??????????????????")
                }
                val freePlaces = remember(state.freePlaces) {
                    state.freePlaces.toList()
                }
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp)) {
                    items(freePlaces) { item ->
                        var checked by remember { mutableStateOf(false) }
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .clickable { checked = !checked }) {
                            Text(
                                text = item.first.title,
                                style = MaterialTheme3.typography.titleSmall
                            )
                            WithContentAlpha(alpha = ContentAlpha.medium) {
                                Text(
                                    text = "?????????????? ?? ?????? ??????????: " + item.second.toString(),
                                    style = MaterialTheme3.typography.bodySmall
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
                positiveButton("????")
                negativeButton("????????????")
            }
        ) {
            datepicker(title = "???????????????? ????????") { date ->
                onDateSelect(date)
            }
        }

        MaterialDialog(
            dialogState = dialogTimePickerFromState,
            buttons = {
                positiveButton("????")
                negativeButton("????????????")
            }
        ) {
            timepicker(title = "???????????????? ?????????????????? ??????????", is24HourClock = true) { time ->
                onTimeFromSelect(time)
            }
        }

        MaterialDialog(
            dialogState = dialogTimePickerToState,
            buttons = {
                positiveButton("????")
                negativeButton("????????????")
            }
        ) {
            timepicker(title = "?????????????? ???????????????? ??????????", is24HourClock = true) { time ->
                onTimeToSelect(time)
            }
        }
    }
}