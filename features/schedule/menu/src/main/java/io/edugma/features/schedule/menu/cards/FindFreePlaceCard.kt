package io.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.edugma.core.designSystem.organism.actionCard.EdActionCardWidth
import io.edugma.core.designSystem.organism.iconCard.EdIconCard
import io.edugma.core.designSystem.utils.rememberCachedIconPainter
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.schedule.menu.R

@Composable
fun FindFreePlaceCard(
    onFreePlaceClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdIconCard(
        title = stringResource(R.string.sch_find_free_place),
        onClick = onFreePlaceClick,
        width = EdActionCardWidth.medium,
        icon = rememberCachedIconPainter("https://img.icons8.com/fluency/48/school-building.png"),
        modifier = modifier,
    )
}
