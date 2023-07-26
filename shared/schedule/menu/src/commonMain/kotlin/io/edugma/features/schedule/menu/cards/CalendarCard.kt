package io.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.api.utils.capitalized
import io.edugma.core.api.utils.format
import io.edugma.core.designSystem.organism.iconCard.EdIconCard
import io.edugma.core.designSystem.utils.rememberCachedIconPainter
import io.edugma.core.resources.MR
import io.edugma.core.utils.ClickListener
import kotlinx.datetime.LocalDate

@Composable
fun CalendarCard(
    date: LocalDate,
    onScheduleCalendarClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdIconCard(
        title = stringResource(MR.strings.sch_calendar),
        subtitle = date.format("EEEE").capitalized(),
        onClick = onScheduleCalendarClick,
        icon = rememberCachedIconPainter(
            "https://img.icons8.com/fluency/48/calendar-${date.dayOfMonth}.png",
        ),
        modifier = modifier,
    )
}
