package io.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.edugma.core.designSystem.organism.actionCard.EdActionCard
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.schedule.menu.R

@Composable
fun LessonsReviewCard(
    onLessonsReviewClick: ClickListener,
) {
    EdActionCard(
        title = stringResource(R.string.sch_lessons_review),
        onClick = onLessonsReviewClick,
    ) {
    }
}
