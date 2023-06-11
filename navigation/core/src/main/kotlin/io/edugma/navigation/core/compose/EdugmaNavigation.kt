package io.edugma.navigation.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.edugma.navigation.core.graph.ScreenGraphBuilder
import io.edugma.navigation.core.graph.ScreenModule
import io.edugma.navigation.core.navigator.EdugmaNavigator
import io.edugma.navigation.core.screen.ScreenBundle

@Composable
fun EdugmaNavigation(
    navigator: EdugmaNavigator = rememberEdugmaNavigator(emptyList()),
    firstScreen: ScreenBundle,
) {
    val currentScreen by navigator.currentScreen.collectAsState()

    LaunchedEffect(key1 = Unit) {
        navigator.navigateTo(firstScreen)
    }

    currentScreen?.let {
        it.ui(it.screenBundle)
    }
}

@Composable
fun EdugmaTabNavigation(
    navigator: EdugmaNavigator = rememberEdugmaNavigator(emptyList()),
    firstScreen: ScreenBundle,
) {
    val currentScreen by navigator.currentScreen.collectAsState()

    LaunchedEffect(key1 = Unit) {
        navigator.navigateTo(firstScreen)
    }

    currentScreen?.let {
        it.ui(it.screenBundle)
    }
}

@Composable
fun rememberEdugmaNavigator(screens: List<ScreenModule>): EdugmaNavigator {
    return remember(screens) {
        val screenGraphBuilder = ScreenGraphBuilder()
        screens.forEach { it.invoke(screenGraphBuilder) }
        EdugmaNavigator(screenGraphBuilder)
    }
}
