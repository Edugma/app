package io.edugma.core.designSystem.tokens.elevation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
data class EdElevation(
    val level: Int,
    val defaultElevation: Dp,
    val pressedElevation: Dp,
    val focusedElevation: Dp,
    val hoveredElevation: Dp,
    val draggedElevation: Dp,
    val disabledElevation: Dp,
) {
    fun getNextLevel(): EdElevation {
        return levels.getOrNull(level + 1) ?: this
    }

    companion object {
        val Level0 = EdElevation(
            level = 0,
            defaultElevation = ElevationTokens.Level0,
            pressedElevation = ElevationTokens.Level0,
            focusedElevation = ElevationTokens.Level0,
            hoveredElevation = ElevationTokens.Level1,
            draggedElevation = ElevationTokens.Level3,
            disabledElevation = ElevationTokens.Level0,
        )

        val Level1 = EdElevation(
            level = 1,
            defaultElevation = ElevationTokens.Level1,
            pressedElevation = ElevationTokens.Level1,
            focusedElevation = ElevationTokens.Level1,
            hoveredElevation = ElevationTokens.Level2,
            draggedElevation = ElevationTokens.Level4,
            disabledElevation = ElevationTokens.Level1,
        )

        val Level2 = EdElevation(
            level = 2,
            defaultElevation = ElevationTokens.Level2,
            pressedElevation = ElevationTokens.Level2,
            focusedElevation = ElevationTokens.Level2,
            hoveredElevation = ElevationTokens.Level3,
            draggedElevation = ElevationTokens.Level5,
            disabledElevation = ElevationTokens.Level2,
        )

        val Level3 = EdElevation(
            level = 3,
            defaultElevation = ElevationTokens.Level3,
            pressedElevation = ElevationTokens.Level3,
            focusedElevation = ElevationTokens.Level3,
            hoveredElevation = ElevationTokens.Level4,
            draggedElevation = ElevationTokens.Level5,
            disabledElevation = ElevationTokens.Level3,
        )

        private val levels = listOf(
            Level0,
            Level1,
            Level2,
            Level3,
        )
    }
}
