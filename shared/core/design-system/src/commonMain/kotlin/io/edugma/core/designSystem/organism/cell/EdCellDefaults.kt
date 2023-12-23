package io.edugma.core.designSystem.organism.cell

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object EdCellDefaults {
    fun contentPadding(
        top: Dp = 10.dp,
        bottom: Dp = 10.dp,
        start: Dp = 6.dp,
        end: Dp = 10.dp,
    ) = PaddingValues(
        top = top,
        bottom = bottom,
        start = start,
        end = end,
    )
}
