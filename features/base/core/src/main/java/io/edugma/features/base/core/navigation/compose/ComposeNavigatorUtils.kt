package io.edugma.features.base.core.navigation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.edugma.features.base.core.navigation.core.Router
import io.edugma.navigation.core.compose.rememberEdugmaNavigator
import io.edugma.navigation.core.graph.ScreenModule
import io.edugma.navigation.core.navigator.EdugmaNavigator

@Composable
fun rememberRouterNavigator(
    router: Router,
    screens: List<ScreenModule>,
): EdugmaNavigator {
    val edugmaNavigator = rememberEdugmaNavigator(screens)
    val navigator = remember(edugmaNavigator, router) {
        ComposeNavigator(edugmaNavigator, router)
    }

    return navigator.edugmaNavigator
}
