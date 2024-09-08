package com.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.edugma.core.api.utils.DateFormat
import com.edugma.core.api.utils.capitalized
import com.edugma.core.api.utils.format
import com.edugma.core.designSystem.organism.iconCard.EdIconCard
import com.edugma.core.designSystem.utils.rememberCachedIconPainter
import com.edugma.core.utils.ClickListener
import edugma.shared.core.icons.generated.resources.*
import edugma.shared.core.resources.generated.resources.*
import edugma.shared.core.resources.generated.resources.Res
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource

@Composable
fun CalendarCard(
    date: LocalDate,
    onScheduleCalendarClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdIconCard(
        title = stringResource(Res.string.sch_calendar),
        subtitle = date.format(DateFormat.WEEK).capitalized(),
        onClick = onScheduleCalendarClick,
        icon = rememberCachedIconPainter(
            "https://img.icons8.com/fluency/48/calendar-${date.dayOfMonth}.png",
        ),
        modifier = modifier,
    )
}
