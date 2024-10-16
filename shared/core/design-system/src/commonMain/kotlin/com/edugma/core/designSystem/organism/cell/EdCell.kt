package com.edugma.core.designSystem.organism.cell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.edugma.core.api.utils.getInitials
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.molecules.avatar.EdAvatar
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.utils.SecondaryContent
import com.edugma.core.designSystem.utils.edPlaceholder
import com.edugma.core.designSystem.utils.getPaletteColor
import com.edugma.core.designSystem.utils.ifNotNull

@Composable
fun EdCell(
    title: String,
    subtitle: String,
    avatar: String?,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    size: EdCellSize = EdCellSize.medium,
    contentPadding: PaddingValues = EdCellDefaults.contentPadding(),
    endContent: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clip(EdTheme.shapes.small)
            .ifNotNull(onClick) {
                clickable(onClick = onClick!!)
            }
            .fillMaxWidth()
            .padding(contentPadding),
    ) {
        EdAvatar(
            url = avatar,
            initials = getInitials(title),
            placeholderColor = getPaletteColor(title),
            size = size.avatarSize,
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
        ) {
            val maxLines = if (subtitle.isEmpty()) {
                3
            } else {
                1
            }
            EdLabel(
                text = title,
                style = size.titleTextStyle,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
            )
            if (subtitle.isNotEmpty()) {
                SecondaryContent {
                    EdLabel(
                        modifier = Modifier.padding(top = size.titleSubtitleSpacing),
                        text = subtitle,
                        style = size.subtitleTextStyle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        Box(modifier = Modifier.align(Alignment.CenterVertically)) {
            endContent()
        }
    }
}

@Composable
fun EdCellPlaceholder(
    modifier: Modifier = Modifier,
    size: EdCellSize = EdCellSize.medium,
) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(EdTheme.shapes.small)
            .padding(horizontal = 6.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(size.avatarSize.size)
                .edPlaceholder(),
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth(),
        ) {
            val titleHeight = with(LocalDensity.current) {
                size.titleTextStyle.fontSize.toDp()
            }
            val subtitleHeight = with(LocalDensity.current) {
                size.subtitleTextStyle.fontSize.toDp()
            }
            Box(
                modifier = Modifier
                    .widthIn(max = 200.dp)
                    .fillMaxWidth()
                    .height(titleHeight)
                    .edPlaceholder(),
            )
            Box(
                modifier = Modifier
                    .padding(top = size.titleSubtitleSpacing * 2.5f)
                    .widthIn(max = 150.dp)
                    .fillMaxWidth()
                    .height(subtitleHeight)
                    .edPlaceholder(),
            )
        }
    }
}
