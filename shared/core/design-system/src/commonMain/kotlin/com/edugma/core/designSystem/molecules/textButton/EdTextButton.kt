package com.edugma.core.designSystem.molecules.textButton

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.theme.EdTheme

@Composable
fun EdTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = EdTheme.colorScheme.primary,
    size: EdTextButtonSize = EdTextButtonSize.medium,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    EdLabel(
        text = text,
        modifier = modifier
            .clip(size.shape)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                role = Role.Button,
                indication = LocalIndication.current,
                onClick = onClick,
            ).padding(size.contentPadding),
        style = size.textStyle,
        color = color,
        overflow = TextOverflow.Ellipsis,
    )
}
