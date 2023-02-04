package io.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.edugma.core.designSystem.organism.accountSelector.AccountSelectorVO
import io.edugma.core.designSystem.organism.accountSelector.EdAccountSelector
import io.edugma.features.base.core.utils.ClickListener

@Composable
fun ScheduleSourcesCard(
    accountSelectorVO: AccountSelectorVO,
    onScheduleSourceClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdAccountSelector(
        state = accountSelectorVO,
        onClick = onScheduleSourceClick,
    )
}
