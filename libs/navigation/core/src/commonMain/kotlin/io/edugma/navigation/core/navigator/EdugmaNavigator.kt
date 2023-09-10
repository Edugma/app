package io.edugma.navigation.core.navigator

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.destroy
import io.edugma.navigation.core.graph.ScreenGraphBuilder
import io.edugma.navigation.core.screen.Screen
import io.edugma.navigation.core.screen.ScreenBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EdugmaNavigator(
    screenGraphBuilder: ScreenGraphBuilder,
    firstScreen: ScreenBundle,
) {
    val backStack: MutableList<ScreenUiState> = mutableListOf<ScreenUiState>()

    private val screenToUI: Map<Screen, @Composable (ScreenBundle) -> Unit>

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _currentScreen: MutableStateFlow<ScreenUiState>

    init {
        screenToUI = screenGraphBuilder.map
        val ui = checkNotNull(screenToUI[firstScreen.screen])
        val state = ScreenUiState(
            screenBundle = firstScreen,
            ui = ui,
        )

        backStack += state

        _currentScreen = MutableStateFlow<ScreenUiState>(state)
        onCreateScreen(state)
    }

    val currentScreen = _currentScreen.asStateFlow()

    fun navigateTo(screenBundle: ScreenBundle, singleTop: Boolean = false) {
        val ui = checkNotNull(screenToUI[screenBundle.screen])

        val state = if (singleTop) {
            remove(screenBundle.screen) ?: ScreenUiState(
                screenBundle = screenBundle,
                ui = ui,
            )
        } else {
            ScreenUiState(
                screenBundle = screenBundle,
                ui = ui,
            )
        }

        backStack += state
        updateCurrentScreen()
    }

    private fun remove(screen: Screen): ScreenUiState? {
        val index = backStack.indexOfFirst { it.screenBundle.screen == screen }
        if (index != -1) {
            return backStack.removeAt(index)
        }
        return null
    }

    fun back(): Boolean {
        return if (backStack.size <= 1) {
            false
        } else {
            removeLastBackStack()
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
            removeLastBackStack()
        }
        updateCurrentScreen()
    }

    private fun updateCurrentScreen() {
        onDestroyScreen(_currentScreen.value)
        _currentScreen.value = backStack.last().copy(
            lifecycleRegistry = LifecycleRegistry(),
        )
        onCreateScreen(_currentScreen.value)
    }

    private fun onCreateScreen(screen: ScreenUiState) {
        screen.lifecycleRegistry.create()
    }

    private fun onDestroyScreen(screen: ScreenUiState) {
        screen.lifecycleRegistry.destroy()
    }

    private fun removeLastBackStack() {
        val screen = backStack.removeLast()
        screen.instanceKeeperDispatcher.destroy()
    }
}
