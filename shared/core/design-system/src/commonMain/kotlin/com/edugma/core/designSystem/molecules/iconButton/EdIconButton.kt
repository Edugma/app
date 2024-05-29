package com.edugma.core.designSystem.molecules.iconButton

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.edugma.core.designSystem.atoms.loader.EdLoader
import com.edugma.core.designSystem.atoms.loader.EdLoaderStyle
import com.edugma.core.designSystem.molecules.button.EdButtonStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdIconButton(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: EdButtonStyle = EdButtonStyle.primary,
    size: EdIconButtonSize = EdIconButtonSize.medium,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false,
    ) {
        FilledIconButton(
            onClick = onClick,
            modifier = modifier
                .size(size.size),
            enabled = enabled,
            interactionSource = interactionSource,
            shape = size.shape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = style.containerColor,
                contentColor = style.contentColor,
            ),
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
            )

            if (isLoading) {
                EdLoader(
                    size = size.loaderSize,
                    style = EdLoaderStyle.content,
                )
            }
        }
    }
}
