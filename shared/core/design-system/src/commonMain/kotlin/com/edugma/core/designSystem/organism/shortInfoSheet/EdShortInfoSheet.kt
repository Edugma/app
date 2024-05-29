package com.edugma.core.designSystem.organism.shortInfoSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edugma.core.api.utils.getInitials
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.molecules.avatar.EdAvatar
import com.edugma.core.designSystem.molecules.avatar.EdAvatarSize
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.utils.SecondaryContent
import com.edugma.core.designSystem.utils.getPaletteColor

@Composable
fun EdShortInfoSheet(
    title: String,
    description: String,
    avatar: String?,
    actionsContent: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(
                horizontal = 16.dp,
                vertical = 5.dp,
            ),
    ) {
        EdAvatar(
            url = avatar,
            initials = getInitials(title),
            placeholderColor = getPaletteColor(title),
            size = EdAvatarSize.xxxl,
            modifier = Modifier
                .padding(top = 10.dp)
                .align(Alignment.CenterHorizontally),
        )
        SpacerHeight(height = 10.dp)
        EdLabel(
            text = title,
            style = EdTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
        )
        if (description.isNotEmpty()) {
            SecondaryContent {
                EdLabel(
                    modifier = Modifier.padding(top = 10.dp),
                    text = description,
                    style = EdTheme.typography.bodyMedium,
                )
            }
        }
        SpacerHeight(height = 16.dp)
        actionsContent()
    }
}
