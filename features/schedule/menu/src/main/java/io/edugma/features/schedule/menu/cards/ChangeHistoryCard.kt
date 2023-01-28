package io.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import io.edugma.core.designSystem.organism.actionCard.EdActionCard
import io.edugma.features.base.core.utils.ClickListener

@Composable
fun ChangeHistoryCard(
    onLessonsReviewClick: ClickListener,
) {
    EdActionCard(
        title = "История изменений",
        onClick = onLessonsReviewClick,
    ) {
    }
}
