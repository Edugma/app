package com.edugma.navigation.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)

// TODO Возможно стоит явно прокидывать state
val LocalNavigationBackHandler = staticCompositionLocalOf<NavigationBackHandler> { error("") }

class NavigationBackHandler(
    val onCantBack: () -> Unit,
)
