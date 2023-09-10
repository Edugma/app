package io.edugma.navigation.core.compose

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.edugma.navigation.core.graph.ScreenGraphBuilder
import io.edugma.navigation.core.graph.ScreenModule
import io.edugma.navigation.core.instanceKeeper.LocalInstanceKeeperOwner
import io.edugma.navigation.core.lifecycle.LocalScreenLifecycleOwner
import io.edugma.navigation.core.navigator.EdugmaNavigator
import io.edugma.navigation.core.screen.ScreenBundle

@Composable
fun EdugmaNavigation(
    navigator: EdugmaNavigator,
) {
    val currentScreen by navigator.currentScreen.collectAsState()
    val navigationBackHandler = LocalNavigationBackHandler.current

    BackHandler {
        val canNavigateBack = navigator.back()
        if (!canNavigateBack) {
            navigationBackHandler.onCantBack()
        }
    }
    Crossfade(targetState = currentScreen) { screenUiState ->
        CompositionLocalProvider(
            LocalScreenLifecycleOwner provides screenUiState,
            LocalInstanceKeeperOwner provides screenUiState,
        ) {
            screenUiState.ui(screenUiState.screenBundle)
        }
    }
}

@Composable
fun EdugmaTabNavigation(
    navigator: EdugmaNavigator,
) {
    val currentScreen by navigator.currentScreen.collectAsState()

    val navigationBackHandler = remember {
        NavigationBackHandler(
            onCantBack = {
                navigator.back()
            },
        )
    }

    CompositionLocalProvider(LocalNavigationBackHandler provides navigationBackHandler) {
        Crossfade(targetState = currentScreen) { screenUiState ->
            screenUiState.ui(screenUiState.screenBundle)
            DisposableEffect(screenUiState) {
                onDispose {
                    navigator.onScreenChanged(screenUiState)
                }
            }
        }
    }
}

@Composable
fun rememberEdugmaNavigator(
    screens: List<ScreenModule>,
    firstScreen: ScreenBundle,
): EdugmaNavigator {
    return remember(screens) {
        val screenGraphBuilder = ScreenGraphBuilder()
        screens.forEach { it.invoke(screenGraphBuilder) }
        EdugmaNavigator(screenGraphBuilder, firstScreen)
    }
}
