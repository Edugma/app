package io.edugma.core.navigation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.edugma.navigation.core.compose.rememberEdugmaNavigator
import io.edugma.navigation.core.graph.ScreenModule
import io.edugma.navigation.core.navigator.EdugmaNavigator
import io.edugma.navigation.core.screen.ScreenBundle

@Composable
fun rememberRouterNavigator(
    router: Router,
    screens: List<ScreenModule>,
    firstScreen: ScreenBundle,
    isActive: () -> Boolean,
): EdugmaNavigator {
    val edugmaNavigator = rememberEdugmaNavigator(screens, firstScreen)
    val navigator = remember(edugmaNavigator, router, isActive) {
        ComposeNavigator(edugmaNavigator, router, isActive)
    }

    return navigator.edugmaNavigator
}
