package com.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.edugma.core.designSystem.organism.iconCard.EdIconCard
import com.edugma.core.designSystem.utils.rememberCachedIconPainter
import com.edugma.core.utils.ClickListener

@Composable
fun ScheduleAppWidgetCard(
    onScheduleWidget: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdIconCard(
        title = "Виджет",
        subtitle = "Настроить",
        onClick = onScheduleWidget,
        modifier = modifier,
        icon = rememberCachedIconPainter("https://img.icons8.com/fluency/48/color-widgets.png"),
    )
}
