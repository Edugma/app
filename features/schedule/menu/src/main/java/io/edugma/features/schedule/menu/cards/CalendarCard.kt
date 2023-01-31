package io.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.edugma.core.designSystem.organism.iconCard.EdIconCard
import io.edugma.core.designSystem.utils.cachedIconPainter
import io.edugma.domain.base.utils.capitalized
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.schedule.menu.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dayOfWeekFormat = DateTimeFormatter.ofPattern("EEEE")

@Composable
fun CalendarCard(
    date: LocalDate,
    onScheduleCalendarClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdIconCard(
        title = stringResource(R.string.sch_calendar),
        subtitle = date.format(dayOfWeekFormat).capitalized(),
        onClick = onScheduleCalendarClick,
        icon = cachedIconPainter(
            "https://img.icons8.com/fluency/48/calendar-${date.dayOfMonth}.png",
        ),
        modifier = modifier,
    )
}
