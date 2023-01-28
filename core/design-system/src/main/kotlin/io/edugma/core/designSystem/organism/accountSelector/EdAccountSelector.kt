package io.edugma.core.designSystem.organism.accountSelector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.molecules.avatar.EdAvatar
import io.edugma.core.designSystem.molecules.avatar.toAvatarInitials
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons

@Composable
fun EdAccountSelector(
    state: AccountSelectorVO,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(EdTheme.shapes.small)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EdAvatar(
            url = state.avatar,
            initials = remember { state.title.toAvatarInitials() },
            modifier = Modifier.size(56.dp),
        )
        SpacerWidth(width = 10.dp)
        Column(
            modifier = Modifier.weight(1f),
        ) {
            EdLabel(
                text = state.title,
                style = EdTheme.typography.titleMedium,
            )
            EdLabel(
                text = state.subtitle,
                maxLines = 2,
                style = EdTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                overflow = TextOverflow.Ellipsis,
            )
        }
        SpacerWidth(width = 4.dp)
        Icon(
            painter = painterResource(EdIcons.ic_fluent_chevron_right_20_filled),
            contentDescription = null,
            tint = EdTheme.colorScheme.outline,
        )
    }
}
