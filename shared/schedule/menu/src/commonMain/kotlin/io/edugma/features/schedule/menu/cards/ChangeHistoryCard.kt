package io.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.edugma.core.designSystem.organism.iconCard.EdIconCard
import io.edugma.core.designSystem.utils.rememberCachedIconPainter
import io.edugma.core.utils.ClickListener

@Composable
fun ChangeHistoryCard(
    onLessonsReviewClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdIconCard(
        title = "История\nизменений",
        onClick = onLessonsReviewClick,
        modifier = modifier,
        icon = rememberCachedIconPainter("https://img.icons8.com/fluency/48/time-machine.png"),
    )
}
