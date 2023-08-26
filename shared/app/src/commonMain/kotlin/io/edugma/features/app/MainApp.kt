package io.edugma.features.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.edugma.core.api.model.ThemeMode
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.features.app.main.MainContent

@Composable
fun MainApp(
    viewModel: MainAppViewModel = getViewModel(),
) {
    val state by viewModel.stateFlow.collectAsState()

    EdTheme(
        useDynamicColors = true,
        useDarkTheme = when (state.themeMode) {
            ThemeMode.Light -> false
            ThemeMode.Dark -> true
            ThemeMode.System -> isSystemInDarkTheme()
        },
    ) {
        EdSurface(
            color = EdTheme.colorScheme.background,
            elevation = EdElevation.Level0,
        ) {
            MainContent()
        }
    }
}
