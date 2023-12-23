package io.edugma.core.designSystem.tokens.colors

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PaletteColorScheme(
    val pomodoro: Color,
    val mandarin: Color,
    val banana: Color,
    val basil: Color,
    val sage: Color,
    val peacock: Color,
    val blueberry: Color,
    val lavender: Color,
    val grape: Color,
    val flamingo: Color,
    val graphite: Color,
)

enum class PaletteColors {
    POMODORO,
    MANDARIN,
    BANANA,
    BASIL,
    SAGE,
    PEACOCK,
    BLUEBERRY,
    LAVENDER,
    GRAPE,
    FLAMINGO,
    GRAPHITE,
}

internal val pomodoroDark = Color(0xffdd5a35)
internal val mandarinDark = Color(0xffe96c43)
internal val bananaDark = Color(0xffebc351)
internal val basilDark = Color(0xff4d9361)
internal val sageDark = Color(0xff4d9361)
internal val peacockDark = Color(0xff4d9361)
internal val blueberryDark = Color(0xff7976c3)
internal val lavenderDark = Color(0xff878dbf)
internal val grapeDark = Color(0xff878dbf)
internal val flamingoDark = Color(0xff878dbf)
internal val graphiteDark = Color(0xff7f8084)

internal val pomodoroLight = Color(0xffeb5540)
internal val mandarinLight = Color(0xfff97b4b)
internal val bananaLight = Color(0xfff8cd53)
internal val basilLight = Color(0xff4f9a65)
internal val sageLight = Color(0xff64c493)
internal val peacockLight = Color(0xff61b1ed)
internal val blueberryLight = Color(0xff7472c1)
internal val lavenderLight = Color(0xff979ed6)
internal val grapeLight = Color(0xffa460b6)
internal val flamingoLight = Color(0xfff29c99)
internal val graphiteLight = Color(0xfff29c99)

internal val darkPaletteColorScheme = PaletteColorScheme(
    pomodoro = pomodoroDark,
    mandarin = mandarinDark,
    banana = bananaDark,
    basil = basilDark,
    sage = sageDark,
    peacock = peacockDark,
    blueberry = blueberryDark,
    lavender = lavenderDark,
    grape = grapeDark,
    flamingo = flamingoDark,
    graphite = graphiteDark,
)

internal val lightPaletteColorScheme = PaletteColorScheme(
    pomodoro = pomodoroLight,
    mandarin = mandarinLight,
    banana = bananaLight,
    basil = basilLight,
    sage = sageLight,
    peacock = peacockLight,
    blueberry = blueberryLight,
    lavender = lavenderLight,
    grape = grapeLight,
    flamingo = flamingoLight,
    graphite = graphiteLight,
)

internal val LocalPaletteColorScheme = staticCompositionLocalOf { lightPaletteColorScheme }
