package io.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.edugma.core.designSystem.organism.actionCard.EdActionCard
import io.edugma.core.designSystem.organism.actionCard.EdActionCardWidth
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.schedule.menu.R

@Composable
fun FindFreePlaceCard(
    onFreePlaceClick: ClickListener,
) {
    EdActionCard(
        title = stringResource(R.string.sch_find_free_place),
        onClick = onFreePlaceClick,
        width = EdActionCardWidth.medium,
    ) {
    }
}
