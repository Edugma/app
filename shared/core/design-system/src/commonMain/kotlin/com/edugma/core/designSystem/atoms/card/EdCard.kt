package com.edugma.core.designSystem.atoms.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.elevation.EdElevation
import com.edugma.core.designSystem.tokens.elevation.LocalEdElevation

@Composable
@NonRestartableComposable
fun EdCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CardDefaults.shape,
    colors: EdCardColors = EdCardDefaults.cardColors(
        containerColor = EdTheme.colorScheme.surface,
    ),
    elevation: EdElevation = LocalEdElevation.current,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit,
) {
    EdSurface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = colors.containerColor(enabled).value,
        contentColor = colors.contentColor(enabled).value,
        elevation = elevation,
        border = border,
        interactionSource = interactionSource,
    ) {
        Column(
            content = content,
            modifier = Modifier.padding(contentPadding),
        )
    }
}

@Composable
@NonRestartableComposable
fun EdCard(
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CardDefaults.shape,
    colors: EdCardColors = EdCardDefaults.cardColors(
        containerColor = EdTheme.colorScheme.surface,
    ),
    elevation: EdElevation = LocalEdElevation.current,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit,
) {
    EdSurface(
        onClick = onClick,
        selected = selected,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = colors.containerColor(enabled).value,
        contentColor = colors.contentColor(enabled).value,
        elevation = elevation,
        border = border,
        interactionSource = interactionSource,
    ) {
        Column(
            content = content,
            modifier = Modifier.padding(contentPadding),
        )
    }
}

@Composable
@NonRestartableComposable
fun EdCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: EdCardColors = EdCardDefaults.cardColors(
        containerColor = EdTheme.colorScheme.surface,
    ),
    elevation: EdElevation = LocalEdElevation.current,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable ColumnScope.() -> Unit,
) {
    EdSurface(
        modifier = modifier,
        shape = shape,
        color = colors.containerColor(enabled = true).value,
        contentColor = colors.contentColor(enabled = true).value,
        elevation = elevation,
        border = border,
    ) {
        Column(
            content = content,
            modifier = Modifier.padding(contentPadding),
        )
    }
}

@Composable
private fun EdElevation.toCardElevation(): CardElevation {
    return CardDefaults.cardElevation(
        defaultElevation,
        pressedElevation,
        focusedElevation,
        hoveredElevation,
        draggedElevation,
        disabledElevation,
    )
}
