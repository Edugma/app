package io.edugma.navigation.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.activity.compose.BackHandler as AndroidBackHandler

@Composable
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    AndroidBackHandler(enabled = enabled, onBack = onBack)
}

// ios
// @Composable
// fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
//
// }

// TODO Возможно стоит явно прокидывать state
val LocalNavigationBackHandler = staticCompositionLocalOf<NavigationBackHandler> { error("") }

class NavigationBackHandler(
    val onCantBack: () -> Unit,
)
