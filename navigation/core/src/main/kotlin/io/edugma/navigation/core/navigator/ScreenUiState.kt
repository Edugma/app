package io.edugma.navigation.core.navigator

import androidx.compose.runtime.Composable
import io.edugma.navigation.core.screen.ScreenBundle

class ScreenUiState(
    val screenBundle: ScreenBundle,
    val ui: @Composable (ScreenBundle) -> Unit,
)
