package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.dp
import io.edugma.features.schedule.menu.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dayOfWeekFormat = DateTimeFormatter.ofPattern("EEEE")
private val dayOfMonthFormat = DateTimeFormatter.ofPattern("dd")
private val monthFormat = DateTimeFormatter.ofPattern("MMMM")

@Composable
fun CalendarCard(
    date: LocalDate,
    onScheduleCalendarClick: ClickListener,
) {
    EdCard(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(150.dp)
            .fillMaxWidth(),
        onClick = onScheduleCalendarClick,
    ) {
        Column(
            Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.sch_calendar),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = date.format(dayOfWeekFormat).uppercase(),
                        style = EdTheme.typography.labelMedium,
                        color = EdTheme.colorScheme.tertiary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = date.format(dayOfMonthFormat),
                        style = EdTheme.typography.displayMedium,
                        color = EdTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = -EdTheme.typography.displayMedium.fontSize.dp() * 0.2f)
                            .height(EdTheme.typography.displayMedium.fontSize.dp() * 1.2f),
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = date.format(monthFormat).lowercase(),
                        style = EdTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = -EdTheme.typography.displayMedium.fontSize.dp() * 0.2f),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
