package io.edugma.features.schedule.daily

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.card.EdCardDefaults
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.utils.ContentAlpha
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.core.utils.WithContentAlpha
import io.edugma.features.base.core.utils.disabledHorizontalPointerInputScroll
import io.edugma.features.base.core.utils.isItemFullyVisible
import io.edugma.features.base.core.utils.sp
import io.edugma.features.schedule.daily.model.DayUiModel
import io.edugma.features.schedule.daily.model.WeekUiModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DaysPager(
    weeks: List<WeekUiModel>,
    dayOfWeekPos: Int,
    pagerState: PagerState,
    onDayClick: Typed1Listener<LocalDate>,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        count = weeks.size,
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .height(82.dp)
            .disabledHorizontalPointerInputScroll(),
        flingBehavior = object : FlingBehavior {
            override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                return initialVelocity
            }
        },
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

private val weekFormat = DateTimeFormatter.ofPattern("EEE")

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
                text = weekFormat.format(day.date).uppercase(),
                style = MaterialTheme.typography.labelSmall,
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
                    style = MaterialTheme.typography.titleSmall,
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
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                    )
                }
            }
        } else {
            Surface(
                modifier = Modifier.height(14.dp),
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        modifier = Modifier.padding(bottom = 1.dp, start = 5.dp, end = 5.dp),
                        text = day.lessonCount.toString(),
                        style = EdTheme.typography.titleSmall,
                        fontSize = 9.0.dp.sp(),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}
