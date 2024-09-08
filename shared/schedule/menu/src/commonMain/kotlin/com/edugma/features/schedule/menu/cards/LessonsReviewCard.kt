package com.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.edugma.core.designSystem.organism.iconCard.EdIconCard
import com.edugma.core.designSystem.utils.rememberCachedIconPainter
import com.edugma.core.utils.ClickListener
import edugma.shared.core.icons.generated.resources.*
import edugma.shared.core.resources.generated.resources.*
import edugma.shared.core.resources.generated.resources.Res
import org.jetbrains.compose.resources.stringResource

@Composable
fun LessonsReviewCard(
    onLessonsReviewClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdIconCard(
        title = stringResource(Res.string.sch_lessons_review),
        onClick = onLessonsReviewClick,
        modifier = modifier,
        icon = rememberCachedIconPainter("https://img.icons8.com/fluency/48/prototype.png"),
    )
}
