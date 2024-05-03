package io.edugma.navigation.core.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import io.edugma.navigation.core.screen.Screen
import io.edugma.navigation.core.screen.ScreenBundle

@Immutable
data class NavigationUiState(
    val backStack: List<ScreenUiState> = emptyList(),
) {
    val currentScreen: ScreenUiState
        get() = backStack.last()

    fun clearExpectFirst(): NavigationUiState {
        val first = backStack.first()
        deactivateScreen(currentScreen)

        for (removed in backStack) {
            processRemovedScreen(removed)
        }
        return copy(backStack = listOf(first))
            .activateCurrentScreen()
    }

    private fun replaceLastBackStack(screen: ScreenUiState): NavigationUiState {
        val lastIndex = backStack.lastIndex
        val newBackStack = backStack.mapIndexed { index, screenUiState ->
            if (index == lastIndex) {
                screen
            } else {
                screenUiState
            }
        }
        return copy(
            backStack = newBackStack,
        )
    }

    private fun activateCurrentScreen(): NavigationUiState {
        val newCurrentScreen = backStack.last().lifecycleReset()
        newCurrentScreen.lifecycleRegistry.create()
        return replaceLastBackStack(newCurrentScreen)
    }

    fun removeCurrentScreen(): NavigationUiState {
        val lastIndex = backStack.lastIndex
        val removed = backStack[lastIndex]
        val newBackStack = backStack.filterIndexed { i, _ -> i != lastIndex }

        deactivateScreen(removed)
        processRemovedScreen(removed)

        return copy(
            backStack = newBackStack,
        ).activateCurrentScreen()
    }

    private fun removeAtBackStack(index: Int, activate: Boolean = true): NavigationUiState {
        val removed = backStack[index]
        val isCurrentScreen = index == backStack.lastIndex
        val newBackStack = backStack.filterIndexed { i, _ -> i != index }

        if (isCurrentScreen) {
            deactivateScreen(removed)
        }
        processRemovedScreen(removed)

        return copy(
            backStack = newBackStack,
        ).run {
            if (isCurrentScreen && activate) {
                activateCurrentScreen()
            } else {
                this
            }
        }
    }

    fun newCurrentScreen(
        ui: @Composable (ScreenBundle) -> Unit,
        screenBundle: ScreenBundle,
        singleTop: Boolean = false,
    ): NavigationUiState {
        val newState: NavigationUiState
        val screenUiState = if (singleTop) {
            val (screen, newState2) = remove(screenBundle.screen)
            newState = newState2
            screen ?: ScreenUiState(
                screenBundle = screenBundle,
                ui = ui,
            )
        } else {
            newState = this
            ScreenUiState(
                screenBundle = screenBundle,
                ui = ui,
            )
        }
        return newState.addToBackStack(screenUiState)
    }

    private fun remove(screen: Screen): Pair<ScreenUiState?, NavigationUiState> {
        val index = backStack.indexOfFirst { it.screenBundle.screen == screen }
        if (index != -1) {
            val removed = backStack[index]
            return removed to removeAtBackStack(index, false)
        }
        return null to this
    }

    private fun addToBackStack(screen: ScreenUiState) =
        apply {
            deactivateScreen(currentScreen)
        }.copy(backStack = backStack + screen)
            .activateCurrentScreen()

    fun removeLastUntil(screen: Screen, include: Boolean): NavigationUiState {
        val lastIndex = backStack.lastIndex
        var indexOfScreen = -1
        for (index in lastIndex downTo 0) {
            if (backStack[index].screenBundle.screen == screen) {
                indexOfScreen = index
                break
            }
        }
        if (indexOfScreen == -1) {
            return this
        }
        val fixedIndex = if (include) {
            indexOfScreen
        } else {
            indexOfScreen + 1
        }
        if (fixedIndex > lastIndex) {
            return this
        }

        return removeLast(lastIndex - fixedIndex + 1)
    }

    private fun removeLast(amount: Int): NavigationUiState {
        if (amount == 0) return this

        deactivateScreen(currentScreen)

        val fixedAmount = amount.coerceAtMost(backStack.size)
        val newLastIndex = backStack.lastIndex - fixedAmount
        val newRemovedFirstIndex = (newLastIndex + 1).coerceAtMost(backStack.lastIndex)

        for (i in newRemovedFirstIndex until backStack.lastIndex) {
            processRemovedScreen(backStack[i])
        }

        val newBackStack = backStack.subList(0, newLastIndex + 1)

        return copy(
            backStack = newBackStack,
        ).activateCurrentScreen()
    }

    private fun deactivateScreen(screen: ScreenUiState) {
        screen.lifecycleDestroy()
    }

    private fun processRemovedScreen(screen: ScreenUiState) {
        screen.viewModelStore.clear()
    }

    companion object {
        fun create(startScreen: ScreenUiState): NavigationUiState {
            val state = NavigationUiState(
                backStack = listOf(startScreen),
            )
            startScreen.lifecycleCreate()

            return state
        }
    }
}
