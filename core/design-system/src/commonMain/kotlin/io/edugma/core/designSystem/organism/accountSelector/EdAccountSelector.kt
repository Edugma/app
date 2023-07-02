package io.edugma.core.designSystem.organism.accountSelector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.molecules.avatar.EdAvatar
import io.edugma.core.designSystem.molecules.avatar.EdAvatarSize
import io.edugma.core.designSystem.molecules.avatar.toAvatarInitials
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.edPlaceholder
import io.edugma.core.icons.EdIcons
import org.jetbrains.compose.resources.ExperimentalResourceApi
import dev.icerock.moko.resources.compose.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun EdAccountSelector(
    state: AccountSelectorVO,
    onClick: (() -> Unit)? = null,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(EdTheme.shapes.small)
            .clickable(onClick = { onClick?.invoke() }, enabled = onClick != null)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EdAvatar(
            url = state.avatar,
            initials = state.title.toAvatarInitials(),
            size = EdAvatarSize.large,
        )
        SpacerWidth(width = 10.dp)
        Column(
            modifier = Modifier.weight(1f),
        ) {
            EdLabel(
                text = state.title,
                style = EdTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
            )
            EdLabel(
                text = state.subtitle,
                maxLines = 2,
                style = EdTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (onClick != null) {
            SpacerWidth(width = 4.dp)
            Icon(
                painter = painterResource(EdIcons.ic_fluent_chevron_right_20_filled),
                contentDescription = null,
                tint = EdTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
fun EdAccountSelectorPlaceholder() {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(EdTheme.shapes.small)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EdAvatar(
            url = null,
            size = EdAvatarSize.large,
            modifier = Modifier
                .clip(CircleShape)
                .edPlaceholder(),
        )
        SpacerWidth(width = 10.dp)
        Column(
            modifier = Modifier.weight(1f),
        ) {
            EdLabel(
                text = "state.title",
                style = EdTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .width(100.dp)
                    .edPlaceholder(),
            )
            SpacerHeight(height = 5.dp)
            EdLabel(
                text = "state.subtitle",
                maxLines = 2,
                style = EdTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .width(150.dp)
                    .edPlaceholder(),
            )
        }
    }
}
