package io.edugma.features.app

import androidx.compose.runtime.Composable
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.features.app.main.MainContent

@Composable
fun MainApp() {
    EdTheme(
        useDynamicColors = true,
    ) {
        EdSurface(
            color = EdTheme.colorScheme.background,
            elevation = EdElevation.Level0,
        ) {
            MainContent()
        }
    }
}
