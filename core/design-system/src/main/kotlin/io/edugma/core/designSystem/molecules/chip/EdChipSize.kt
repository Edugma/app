package io.edugma.core.designSystem.molecules.chip

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class EdChipSize(
    val shapeSize: Dp,
) {
    companion object {
        val circle
            get() = EdChipSize(28.0.dp)

        val roundedSquare
            get() = EdChipSize(8.dp)
    }
}
