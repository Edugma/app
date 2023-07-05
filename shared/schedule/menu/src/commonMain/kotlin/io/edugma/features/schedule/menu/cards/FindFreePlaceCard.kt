package io.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.designSystem.organism.actionCard.EdActionCardWidth
import io.edugma.core.designSystem.organism.iconCard.EdIconCard
import io.edugma.core.designSystem.utils.rememberCachedIconPainter
import io.edugma.core.utils.ClickListener
import io.edugma.features.schedule.menu.resources.MR

@Composable
fun FindFreePlaceCard(
    onFreePlaceClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdIconCard(
        title = stringResource(MR.strings.sch_find_free_place),
        onClick = onFreePlaceClick,
        width = EdActionCardWidth.medium,
        icon = rememberCachedIconPainter("https://img.icons8.com/fluency/48/school-building.png"),
        modifier = modifier,
    )
}
