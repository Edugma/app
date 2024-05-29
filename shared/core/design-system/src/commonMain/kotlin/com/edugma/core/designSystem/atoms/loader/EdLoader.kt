package com.edugma.core.designSystem.atoms.loader

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EdLoader(
    modifier: Modifier = Modifier,
    size: EdLoaderSize = EdLoaderSize.small,
    style: EdLoaderStyle = EdLoaderStyle.primary,
) {
    CircularProgressIndicator(
        modifier = modifier.size(size.size),
        color = style.color,
        strokeWidth = size.strokeWidth,
    )
}
