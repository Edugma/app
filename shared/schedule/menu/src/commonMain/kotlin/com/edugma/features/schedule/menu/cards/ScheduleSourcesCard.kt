package com.edugma.features.schedule.menu.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.edugma.core.designSystem.organism.accountSelector.AccountSelectorVO
import com.edugma.core.designSystem.organism.accountSelector.EdAccountSelector
import com.edugma.core.utils.ClickListener

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
