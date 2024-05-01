package io.edugma.core.designSystem.organism.accountSelector

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import edugma.shared.core.icons.generated.resources.ic_fluent_chevron_right_20_filled
import io.edugma.core.designSystem.organism.cell.EdCell
import io.edugma.core.designSystem.organism.cell.EdCellPlaceholder
import io.edugma.core.designSystem.organism.cell.EdCellSize
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.icons.EdIcons
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
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
