package io.edugma.core.designSystem.molecules.chip

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class EdChipForm(
    val shapeSize: Dp,
) {
    companion object {
        val circle
            get() = EdChipForm(28.0.dp)

        val roundedSquare
            get() = EdChipForm(8.dp)
    }
}
