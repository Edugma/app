package io.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.edugma.core.designSystem.organism.iconCard.EdIconCard
import io.edugma.core.designSystem.utils.rememberCachedIconPainter
import io.edugma.core.utils.ClickListener
import io.edugma.domain.base.utils.capitalized
import io.edugma.domain.base.utils.format
import io.edugma.features.schedule.menu.R
import kotlinx.datetime.LocalDate

@Composable
fun CalendarCard(
    date: LocalDate,
    onScheduleCalendarClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdIconCard(
        title = stringResource(R.string.sch_calendar),
        subtitle = date.format("EEEE").capitalized(),
        onClick = onScheduleCalendarClick,
        icon = rememberCachedIconPainter(
            "https://img.icons8.com/fluency/48/calendar-${date.dayOfMonth}.png",
        ),
        modifier = modifier,
    )
}
