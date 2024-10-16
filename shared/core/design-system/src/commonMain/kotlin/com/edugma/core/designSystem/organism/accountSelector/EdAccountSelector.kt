package com.edugma.core.designSystem.organism.accountSelector

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.edugma.core.designSystem.organism.cell.EdCell
import com.edugma.core.designSystem.organism.cell.EdCellPlaceholder
import com.edugma.core.designSystem.organism.cell.EdCellSize
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.icons.EdIcons
import edugma.shared.core.icons.generated.resources.ic_fluent_chevron_right_20_filled
import org.jetbrains.compose.resources.painterResource

@Composable
fun EdAccountSelector(
    state: AccountSelectorVO,
    onClick: (() -> Unit)? = null,
) {
    EdCell(
        title = state.title,
        subtitle = state.subtitle,
        avatar = state.avatar,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        size = EdCellSize.large,
        endContent = {
            if (onClick != null) {
                Icon(
                    painter = painterResource(EdIcons.ic_fluent_chevron_right_20_filled),
                    contentDescription = null,
                    tint = EdTheme.colorScheme.outline,
                )
            }
        },
    )
}

@Composable
fun EdAccountSelectorPlaceholder() {
    EdCellPlaceholder(
        size = EdCellSize.large,
        modifier = Modifier.fillMaxWidth(),
    )
}
