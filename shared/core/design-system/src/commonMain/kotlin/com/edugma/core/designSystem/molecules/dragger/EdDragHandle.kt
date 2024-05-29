package com.edugma.core.designSystem.molecules.dragger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.theme.EdTheme

@Composable
fun EdDragHandle(
    modifier: Modifier = Modifier,
    width: Dp = 35.dp,
) {
    Box(
        modifier = modifier
            .size(height = 5.dp, width = width)
            .background(
                color = EdTheme.colorScheme.surfaceVariant,
                shape = CircleShape,
            ),
    )
}
