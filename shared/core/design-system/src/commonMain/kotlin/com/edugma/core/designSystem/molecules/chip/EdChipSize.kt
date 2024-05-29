package com.edugma.core.designSystem.molecules.chip

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.theme.EdTheme

@Immutable
data class EdChipSize(
    val shape: Shape,
    val textStyle: TextStyle,
    val contentPadding: PaddingValues,
) {
    companion object {
        val small: EdChipSize
            @Composable
            @ReadOnlyComposable
            get() = EdChipSize(
                shape = EdTheme.shapes.extraSmall,
                textStyle = EdTheme.typography.labelMedium,
                contentPadding = PaddingValues(horizontal = 11.dp, vertical = 5.dp),
            )

        val medium: EdChipSize
            @Composable
            @ReadOnlyComposable
            get() = EdChipSize(
                shape = EdTheme.shapes.small,
                textStyle = EdTheme.typography.labelLarge,
                contentPadding = PaddingValues(horizontal = 15.dp, vertical = 7.dp),
            )
    }
}
