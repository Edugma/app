package io.edugma.navigation.core.navigator

import androidx.compose.runtime.Composable
import io.edugma.navigation.core.graph.ScreenGraphBuilder
import io.edugma.navigation.core.screen.Screen
import io.edugma.navigation.core.screen.ScreenBundle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EdugmaNavigator(
    screenGraphBuilder: ScreenGraphBuilder,
    firstScreen: ScreenBundle,
) {
    val backStack: MutableList<ScreenUiState> = mutableListOf<ScreenUiState>()

    private val screenToUI: Map<Screen, @Composable (ScreenBundle) -> Unit>

    private val _firstScreen: ScreenUiState

    init {
        screenToUI = screenGraphBuilder.map
        val ui = checkNotNull(screenToUI[firstScreen.screen])
        val state = ScreenUiState(
            screenBundle = firstScreen,
            ui = ui,
        )

        backStack += state

        _firstScreen = state
    }

    private val _currentScreen = MutableStateFlow<ScreenUiState>(_firstScreen)
    val currentScreen = _currentScreen.asStateFlow()

    fun navigateTo(screenBundle: ScreenBundle, singleTop: Boolean = false) {
        val ui = checkNotNull(screenToUI[screenBundle.screen])
        val state = ScreenUiState(
            screenBundle = screenBundle,
            ui = ui,
        )

        if (singleTop) {
            remove(screenBundle.screen)
        }

        backStack += state
        updateCurrentScreen()
    }

    private fun remove(screen: Screen) {
        val index = backStack.indexOfFirst { it.screenBundle.screen == screen }
        if (index != -1) {
            backStack.removeAt(index)
        }
    }

    fun back(): Boolean {
        return if (backStack.size <= 1) {
            false
        } else {
            backStack.removeLast()
            updateCurrentScreen()
            true
        }
    }

    fun backTo(screen: Screen?) {
        if (screen == null && backStack.size > 1) {
            val first = backStack.first()
            backStack.clear()
            backStack += first
            return
        }

        val lastIndex = backStack.lastIndex
        var indexOfScreen = -1
        for (index in lastIndex downTo 0) {
            if (backStack[index].screenBundle.screen == screen) {
                indexOfScreen = index
                break
            }
        }
        if (indexOfScreen == -1) {
            return
        }

        for (index in lastIndex downTo indexOfScreen) {
            backStack.removeLast()
        }
        updateCurrentScreen()
    }

    private fun updateCurrentScreen() {
        _currentScreen.value = backStack.last()
    }
}
