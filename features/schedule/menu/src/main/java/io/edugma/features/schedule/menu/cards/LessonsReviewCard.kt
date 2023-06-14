package io.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.edugma.core.designSystem.organism.iconCard.EdIconCard
import io.edugma.core.designSystem.utils.rememberCachedIconPainter
import io.edugma.core.utils.ClickListener
import io.edugma.features.schedule.menu.R

@Composable
fun LessonsReviewCard(
    onLessonsReviewClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdIconCard(
        title = stringResource(R.string.sch_lessons_review),
        onClick = onLessonsReviewClick,
        modifier = modifier,
        icon = rememberCachedIconPainter("https://img.icons8.com/fluency/48/prototype.png"),
    )
}
