package io.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.edugma.core.designSystem.organism.iconCard.EdIconCard
import io.edugma.core.designSystem.utils.cachedIconPainter
import io.edugma.features.base.core.utils.ClickListener

@Composable
fun ChangeHistoryCard(
    onLessonsReviewClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdIconCard(
        title = "История изменений",
        onClick = onLessonsReviewClick,
        modifier = modifier,
        icon = cachedIconPainter("https://img.icons8.com/fluency/48/time-machine.png"),
    )
}
