package io.edugma.navigation.core.navigator

import androidx.compose.runtime.Composable
import io.edugma.navigation.core.graph.ScreenGraphBuilder
import io.edugma.navigation.core.screen.Screen
import io.edugma.navigation.core.screen.ScreenBundle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EdugmaNavigator(
    screenGraphBuilder: ScreenGraphBuilder,
    firstScreen: ScreenBundle,
) {
    private val _state: MutableStateFlow<NavigationUiState>
    private val screenToUI: Map<Screen, @Composable (ScreenBundle) -> Unit>

    init {
        screenToUI = screenGraphBuilder.map
        val ui = checkNotNull(screenToUI[firstScreen.screen])
        val state = ScreenUiState(
            screenBundle = firstScreen,
            ui = ui,
        )

        _state = MutableStateFlow(NavigationUiState.create(state))
    }
    val state: StateFlow<NavigationUiState> = _state.asStateFlow()

    fun navigateTo(screenBundle: ScreenBundle, singleTop: Boolean = false) {
        val ui = checkNotNull(screenToUI[screenBundle.screen])

        newState {
            newCurrentScreen(
                ui = ui,
                screenBundle = screenBundle,
                singleTop = singleTop,
            )
        }
    }

    fun back(): Boolean {
        return if (state.value.backStack.size <= 1) {
            false
        } else {
            newState {
                removeCurrentScreen()
            }
            true
        }
    }

    fun backTo(screen: Screen?) {
        if (screen == null) {
            if (state.value.backStack.size > 1) {
                newState {
                    clearExpectFirst()
                }
            }
            return
        }

        newState {
            removeLastUntil(
                screen = screen,
                include = false,
            )
        }
    }

    private inline fun newState(
        newState: NavigationUiState.() -> NavigationUiState,
    ) {
        _state.value = newState(state.value)
    }
}
