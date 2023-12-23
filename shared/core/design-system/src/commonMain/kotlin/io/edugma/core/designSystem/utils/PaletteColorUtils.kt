package io.edugma.core.designSystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.colors.PaletteColors

@Composable
fun getPaletteColor(seed: String): Color {
    val sum = seed.sumOf { it.code }

    val colorCount = PaletteColors.entries.size

    val color = sum % colorCount

    return PaletteColors.entries[color].toColor()
}

@Composable
private fun PaletteColors.toColor(): Color {
    return when (this) {
        PaletteColors.POMODORO -> EdTheme.paletteColorScheme.pomodoro
        PaletteColors.MANDARIN -> EdTheme.paletteColorScheme.mandarin
        PaletteColors.BANANA -> EdTheme.paletteColorScheme.banana
        PaletteColors.BASIL -> EdTheme.paletteColorScheme.basil
        PaletteColors.SAGE -> EdTheme.paletteColorScheme.sage
        PaletteColors.PEACOCK -> EdTheme.paletteColorScheme.peacock
        PaletteColors.BLUEBERRY -> EdTheme.paletteColorScheme.blueberry
        PaletteColors.LAVENDER -> EdTheme.paletteColorScheme.lavender
        PaletteColors.GRAPE -> EdTheme.paletteColorScheme.grape
        PaletteColors.FLAMINGO -> EdTheme.paletteColorScheme.flamingo
        PaletteColors.GRAPHITE -> EdTheme.paletteColorScheme.graphite
    }
}
