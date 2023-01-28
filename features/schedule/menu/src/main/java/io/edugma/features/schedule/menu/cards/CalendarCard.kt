package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.divider.EdDivider
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.actionCard.EdActionCard
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.dp
import io.edugma.features.schedule.menu.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dayOfWeekFormat = DateTimeFormatter.ofPattern("EEE")
private val dayOfMonthFormat = DateTimeFormatter.ofPattern("dd MMMM")

@Composable
fun CalendarCard(
    date: LocalDate,
    onScheduleCalendarClick: ClickListener,
) {
    EdActionCard(
        title = stringResource(R.string.sch_calendar),
        subtitle = date.format(dayOfMonthFormat),
        onClick = onScheduleCalendarClick,
    ) {
        Column(
            modifier = Modifier
                .size(28.dp)
                .background(EdTheme.colorScheme.surface, EdTheme.shapes.extraSmall),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            SpacerHeight(height = 6.dp)
            EdDivider(color = EdTheme.colorScheme.onSurfaceVariant)
            Text(
                text = date.format(dayOfWeekFormat).uppercase(),
                style = EdTheme.typography.labelSmall,
                color = EdTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier,
            )
        }
    }
}
