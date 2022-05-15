package io.edugma.features.schedule.schedule_info.place_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import io.edugma.domain.schedule.model.place.PlaceInfo
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.FluentIcons
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.elements.PrimaryTopAppBar
import io.edugma.features.base.elements.SpacerHeight
import io.edugma.features.base.elements.TextIcon
import io.edugma.features.schedule.schedule_info.R
import org.koin.androidx.compose.getViewModel
import java.time.format.DateTimeFormatter

@Composable
fun PlaceInfoScreen(viewModel: PlaceInfoViewModel = getViewModel(), id: String) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setId(id)
    }

    PlaceInfoContent(
        state = state,
        onBackClick = viewModel::exit,
        onTabSelected = viewModel::onTabSelected,
    )
}

private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
private val dateFormat = DateTimeFormatter.ofPattern("d MMMM yyyy")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceInfoContent(
    state: PlaceInfoState,
    onBackClick: ClickListener,
    onTabSelected: Typed1Listener<PlaceInfoTabs>
) {
    Column(Modifier.fillMaxSize()) {
        PrimaryTopAppBar(
            title = state.placeInfo?.title ?: "",
            onBackClick = onBackClick
        )

        when (state.placeInfo) {
            is PlaceInfo.Building -> PlaceBuilding(
                place = state.placeInfo
            )
            is PlaceInfo.Online -> PlaceOnline(
                place = state.placeInfo
            )
            is PlaceInfo.Other -> PlaceOther(
                place = state.placeInfo
            )
            is PlaceInfo.Unclassified -> PlaceUnclassified(
                place = state.placeInfo
            )
            null -> {  }
        }

        LazyRow(Modifier.fillMaxWidth()) {
            items(state.tabs) {
                Card(
                    onClick = { onTabSelected(it) },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    val text = when (it) {
                        PlaceInfoTabs.Occupancy -> "Занятость"
                        PlaceInfoTabs.Map -> "Карта"
                        PlaceInfoTabs.Schedule -> "Расписание"
                    }

                    Text(
                        text = text,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        SpacerHeight(8.dp)
        when (state.selectedTab) {
            PlaceInfoTabs.Occupancy -> OccupancyTab(state)
            PlaceInfoTabs.Map -> { }
            PlaceInfoTabs.Schedule -> { }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun OccupancyTab(
    state: PlaceInfoState
) {
    HorizontalPager(
        count = state.placeOccupancy.size,
        modifier = Modifier.fillMaxSize()
    ) {
        val currentDay = state.placeOccupancy[it]
        Column(Modifier.fillMaxSize()) {
            Text(text = currentDay.date.format(dateFormat))
            SpacerHeight(10.dp)
            LazyColumn(Modifier.fillMaxWidth()) {
                items(currentDay.values) {
                    Card(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        val time = it.timeFrom.format(timeFormat) +
                                "-" + it.timeTo.format(timeFormat)
                        Text(
                            text = time,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlaceBuilding(place: PlaceInfo.Building) {
    Column(
        Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Text(text = place.title)
        if (place.street != null) {
            SpacerHeight(8.dp)
            TextIcon(
                text = place.street!!,
                painter = painterResource(FluentIcons.ic_fluent_location_20_regular)
            )
        }
        var resStr = ""
        if (place.building != null) {
            resStr += stringResource(R.string.schedule_sch_inf_building, place.building!!)
        }
        if (place.floor != null) {
            if (resStr.isNotEmpty()) {
                resStr += ", "
            }
            resStr += stringResource(R.string.schedule_sch_inf_floor, place.floor!!)
        }
        if (place.auditorium != null) {
            if (resStr.isNotEmpty()) {
                resStr += ", "
            }
            resStr += stringResource(R.string.schedule_sch_inf_auditorium_number, place.auditorium!!)
        }
        if (resStr.isNotEmpty()) {
            SpacerHeight(8.dp)
            TextIcon(
                text = resStr,
                painter = painterResource(FluentIcons.ic_fluent_building_20_regular)
            )
        }
        if (place.description != null) {
            SpacerHeight(8.dp)
            val description = place.description!!.asSequence()
                .joinToString(separator = "\n") { it.key + ": " + it.value }
            TextIcon(
                text = stringResource(R.string.schedule_sch_inf_description),
                painter = painterResource(FluentIcons.ic_fluent_text_description_20_regular)
            )
            SpacerHeight(4.dp)
            Text(
                text = description
            )
        }
    }
}

@Composable
private fun PlaceOnline(place: PlaceInfo.Online) {
    Column(Modifier.fillMaxWidth()) {
        Text(text = place.title)
        if (place.url != null) {
            val uriHandler = LocalUriHandler.current
            val annotatedString = buildAnnotatedString {
                pushStringAnnotation(tag = "url", annotation = place.url!!)
                withStyle(style = SpanStyle(color = MaterialTheme3.colorScheme.primary)) {
                    append(place.url!!)
                }
                pop()
            }

            ClickableText(
                text = annotatedString,
                onClick = {
                    uriHandler.openUri(place.url!!)
                }
            )
        }
        if (place.description != null) {
            SpacerHeight(8.dp)
            val description = place.description!!.asSequence()
                .joinToString(separator = "\n") { it.key + ": " + it.value }
            TextIcon(
                text = stringResource(R.string.schedule_sch_inf_description),
                painter = painterResource(FluentIcons.ic_fluent_text_description_20_regular)
            )
            SpacerHeight(4.dp)
            Text(
                text = description
            )
        }
    }
}

@Composable
private fun PlaceOther(place: PlaceInfo.Other) {
    Column(Modifier.fillMaxWidth()) {
        Text(text = place.title)
        if (place.description != null) {
            SpacerHeight(8.dp)
            val description = place.description!!.asSequence()
                .joinToString(separator = "\n") { it.key + ": " + it.value }
            TextIcon(
                text = stringResource(R.string.schedule_sch_inf_description),
                painter = painterResource(FluentIcons.ic_fluent_text_description_20_regular)
            )
            SpacerHeight(4.dp)
            Text(
                text = description
            )
        }
    }
}

@Composable
private fun PlaceUnclassified(place: PlaceInfo.Unclassified) {
    Column(Modifier.fillMaxWidth()) {
        Text(text = place.title)
        if (place.description != null) {
            SpacerHeight(8.dp)
            val description = place.description!!.asSequence()
                .joinToString(separator = "\n") { it.key + ": " + it.value }
            TextIcon(
                text = stringResource(R.string.schedule_sch_inf_description),
                painter = painterResource(FluentIcons.ic_fluent_text_description_20_regular)
            )
            SpacerHeight(4.dp)
            Text(
                text = description
            )
        }
    }
}