package com.edugma.features.schedule.scheduleInfo.placeInfo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.edugma.core.api.utils.DateFormat
import com.edugma.core.api.utils.TimeFormat
import com.edugma.core.api.utils.format
import com.edugma.core.designSystem.atoms.card.EdCard
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.icons.EdIcons
import com.edugma.core.utils.ClickListener
import com.edugma.core.utils.Typed1Listener
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel
import com.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import com.edugma.features.schedule.elements.verticalSchedule.VerticalScheduleComponent
import com.edugma.features.schedule.scheduleInfo.groupInfo.InfoScaffold
import edugma.shared.core.icons.generated.resources.*
import edugma.shared.core.resources.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import edugma.shared.core.resources.generated.resources.Res as CommonRes

@Composable
fun PlaceInfoScreen(viewModel: PlaceInfoViewModel = getViewModel(), id: String) {
    val state by viewModel.collectAsState()

    LaunchedEffect(id) {
        viewModel.setId(id)
    }

    PlaceInfoContent(
        state = state,
        onBackClick = viewModel::exit,
        onTabSelected = viewModel::onTabSelected,
    )
}

@Composable
private fun PlaceInfoContent(
    state: PlaceInfoState,
    onBackClick: ClickListener,
    onTabSelected: Typed1Listener<PlaceInfoTabs>,
) {
    InfoScaffold(
        title = state.placeInfo?.title ?: "",
        onBackClick = onBackClick,
        fields = {
            state.placeInfo?.let { groupInfo ->
                EdLabel(
                    text = groupInfo.description.orEmpty(),
                    iconPainter = painterResource(EdIcons.ic_fluent_text_description_20_regular),
                )
//                TextIcon(
//                    text = "${groupInfo.course}-й курс",
//                    painter = painterResource(EdIcons.ic_fluent_timer_20_regular)
//                )
//                TextIcon(
//                    text = groupInfo.toString(),
//                    painter = painterResource(EdIcons.ic_fluent_weather_moon_20_regular)
//                )
            }
        },
        tabs = {
            LazyRow(Modifier.fillMaxWidth()) {
                items(state.tabs) {
                    EdCard(
                        onClick = { onTabSelected(it) },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp),
                        shape = EdTheme.shapes.small,
                    ) {
                        val text = when (it) {
                            PlaceInfoTabs.Occupancy -> "Занятость"
                            PlaceInfoTabs.Map -> "Карта"
                            PlaceInfoTabs.Schedule -> "Расписание"
                        }

                        Text(
                            text = text,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        )
                    }
                }
            }
        },
        content = {
            when (state.selectedTab) {
                PlaceInfoTabs.Schedule -> {
                    state.scheduleSource?.let {
                        VerticalScheduleComponent(
                            scheduleSource = state.scheduleSource,
                        )
                    }
                }
                else -> { }
//                GroupInfoTabs.Students -> { }
            }
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OccupancyTab(
    state: PlaceInfoState,
) {
    HorizontalPager(
        state = rememberPagerState { state.placeOccupancy.size },
        modifier = Modifier.fillMaxSize(),
    ) {
        val currentDay = state.placeOccupancy[it]
        Column(Modifier.fillMaxSize()) {
            Text(text = currentDay.date.format(DateFormat.FULL))
            SpacerHeight(10.dp)
            LazyColumn(Modifier.fillMaxWidth()) {
                items(currentDay.values) {
                    Card(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        val time = it.timeFrom.format(TimeFormat.HOURS_MINUTES) +
                            "-" + it.timeTo.format(TimeFormat.HOURS_MINUTES)
                        Text(
                            text = time,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlaceBuilding(place: CompactPlaceInfo.Building) {
    Column(
        Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
    ) {
        Text(text = place.title)
        if (!place.description.isNullOrEmpty()) {
            SpacerHeight(8.dp)
            EdLabel(
                text = place.description.orEmpty(),
                iconPainter = painterResource(EdIcons.ic_fluent_building_20_regular),
            )
        }
        if (place.description != null) {
            SpacerHeight(8.dp)
            val description = place.description!!
            EdLabel(
                text = stringResource(CommonRes.string.schedule_sch_inf_description),
                iconPainter = painterResource(EdIcons.ic_fluent_text_description_20_regular),
            )
            SpacerHeight(4.dp)
            Text(
                text = description,
            )
        }
    }
}

@Composable
private fun PlaceOnline(place: CompactPlaceInfo.Online) {
    Column(Modifier.fillMaxWidth()) {
        Text(text = place.title)
        if (place.url != null) {
            val uriHandler = LocalUriHandler.current
            val annotatedString = buildAnnotatedString {
                pushStringAnnotation(tag = "url", annotation = place.url!!)
                withStyle(style = SpanStyle(color = EdTheme.colorScheme.primary)) {
                    append(place.url!!)
                }
                pop()
            }

            ClickableText(
                text = annotatedString,
                onClick = {
                    uriHandler.openUri(place.url!!)
                },
            )
        }
        if (place.description != null) {
            SpacerHeight(8.dp)
            val description = place.description!!
            EdLabel(
                text = stringResource(CommonRes.string.schedule_sch_inf_description),
                iconPainter = painterResource(EdIcons.ic_fluent_text_description_20_regular),
            )
            SpacerHeight(4.dp)
            Text(
                text = description,
            )
        }
    }
}

@Composable
private fun PlaceOther(place: CompactPlaceInfo.Other) {
    Column(Modifier.fillMaxWidth()) {
        Text(text = place.title)
        if (place.description != null) {
            SpacerHeight(8.dp)
            val description = place.description!!
            EdLabel(
                text = stringResource(CommonRes.string.schedule_sch_inf_description),
                iconPainter = painterResource(EdIcons.ic_fluent_text_description_20_regular),
            )
            SpacerHeight(4.dp)
            Text(
                text = description,
            )
        }
    }
}
