package com.edugma.core.designSystem.molecules.chip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.card.EdCard
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.utils.edPlaceholder

@Composable
fun EdChipLabel(
    text: String,
    modifier: Modifier = Modifier,
    iconPainter: Painter? = null,
    size: EdChipSize = EdChipSize.medium,
) {
    EdCard(
        shape = size.shape,
        modifier = modifier,
        contentPadding = size.contentPadding,
    ) {
        if (iconPainter != null) {
            EdLabel(
                text = text,
                iconPainter = iconPainter,
                maxLines = 1,
                style = size.textStyle,
            )
        } else {
            EdLabel(
                text = text,
                maxLines = 1,
                style = size.textStyle,
            )
        }
    }
}

@Composable
fun EdChipLabel(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconPainter: Painter? = null,
    size: EdChipSize = EdChipSize.medium,
) {
    EdCard(
        onClick = onClick,
        shape = size.shape,
        modifier = modifier,
        contentPadding = size.contentPadding,
    ) {
        if (iconPainter != null) {
            EdLabel(
                text = text,
                iconPainter = iconPainter,
                maxLines = 1,
                style = size.textStyle,
            )
        } else {
            EdLabel(
                text = text,
                maxLines = 1,
                style = size.textStyle,
            )
        }
    }
}

@Composable
fun EdChipLabel(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconPainter: Painter? = null,
    size: EdChipSize = EdChipSize.medium,
) {
    EdCard(
        selected = selected,
        onClick = onClick,
        shape = size.shape,
        modifier = modifier,
        contentPadding = size.contentPadding,
    ) {
        if (iconPainter != null) {
            EdLabel(
                text = text,
                iconPainter = iconPainter,
                maxLines = 1,
                style = size.textStyle,
            )
        } else {
            EdLabel(
                text = text,
                maxLines = 1,
                style = size.textStyle,
            )
        }
    }
}

@Composable
fun EdChipLabelPlaceholder(
    modifier: Modifier = Modifier,
    size: EdChipSize = EdChipSize.medium,
) {
    val density = LocalDensity.current

    val height = with(density) {
        size.contentPadding.calculateBottomPadding() +
            size.contentPadding.calculateBottomPadding() +
            size.textStyle.lineHeight.toDp()
    }

    Box(
        modifier = modifier
            .size(height = height, width = 40.dp)
            .clip(size.shape)
            .edPlaceholder(),
    )
}
