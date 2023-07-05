package io.edugma.features.schedule.daily

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.core.api.utils.format
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.card.EdCardDefaults
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.ContentAlpha
import io.edugma.core.designSystem.utils.WithContentAlpha
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.ui.isItemFullyVisible
import io.edugma.core.utils.ui.sp
import io.edugma.features.schedule.daily.model.DayUiModel
import io.edugma.features.schedule.daily.model.WeekUiModel
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DaysPager(
    weeks: List<WeekUiModel>,
    dayOfWeekPos: Int,
    pagerState: PagerState,
    onDayClick: Typed1Listener<LocalDate>,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        pageCount = weeks.size,
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .height(82.dp),
        userScrollEnabled = false,
    ) {
        val week by remember(weeks, it) { mutableStateOf(weeks[it]) }
        WeekContent(
            week,
            dayOfWeekPos,
            onDayClick = onDayClick,
        )
    }
}

@Composable
fun WeekContent(
    week: WeekUiModel,
    dayOfWeekPos: Int,
    onDayClick: Typed1Listener<LocalDate>,
) {
    val lazyRowState = rememberLazyListState(dayOfWeekPos)

    LaunchedEffect(dayOfWeekPos) {
        if (!lazyRowState.isItemFullyVisible(dayOfWeekPos)) {
            lazyRowState.animateScrollToItem(dayOfWeekPos)
        }
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        week.days.forEachIndexed { index, day ->
            DayContent(
                day,
                dayOfWeekPos == index,
                onDayClick = onDayClick,
            )
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun RowScope.DayContent(
    day: DayUiModel,
    isSelected: Boolean,
    onDayClick: Typed1Listener<LocalDate>,
) {
    val colorFrom = EdTheme.colorScheme.secondary

    val backgroundColor = if (day.isToday) {
        EdTheme.colorScheme.secondaryContainer
    } else {
        EdTheme.colorScheme.surface
    }

    val borderColor by animateColorAsState(
        if (isSelected) colorFrom else Color.Transparent,
        tween(250),
    )
    val border = BorderStroke(1.dp, borderColor)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f),
    ) {
        WithContentAlpha(ContentAlpha.medium) {
            Text(
                text = day.date.format("EEE").uppercase(),
                style = EdTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                textAlign = TextAlign.Center,
            )
        }
        SpacerHeight(height = 1.5.dp)
        EdCard(
            onClick = { onDayClick(day.date) },
            modifier = Modifier
                .padding(start = 3.dp, end = 3.dp, top = 1.dp, bottom = 1.dp)
                .size(39.dp),
            shape = EdTheme.shapes.medium,
            border = border,
            colors = EdCardDefaults.cardColors(containerColor = backgroundColor),
        ) {
            Box(
                Modifier
                    .padding(start = 3.dp, end = 3.dp)
                    .fillMaxSize(),
            ) {
                Text(
                    text = day.date.dayOfMonth.toString(),
                    style = EdTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 1.dp),
                )
            }
        }
        SpacerHeight(height = 2.5.dp)
        if (day.lessonCount < 6) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                (0 until day.lessonCount).forEach {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 1.5.dp)
                            .size(4.5.dp)
                            .background(EdTheme.colorScheme.primary, CircleShape),
                    )
                }
            }
        } else {
            Surface(
                modifier = Modifier.height(14.dp),
                color = EdTheme.colorScheme.primary,
                shape = CircleShape,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        modifier = Modifier.padding(bottom = 1.dp, start = 5.dp, end = 5.dp),
                        text = day.lessonCount.toString(),
                        style = EdTheme.typography.titleSmall,
                        fontSize = 9.0.dp.sp(),
                        color = EdTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}
