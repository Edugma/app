package io.edugma.core.navigation.core

import io.edugma.navigation.core.screen.Screen
import io.edugma.navigation.core.screen.ScreenBundle
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class Router {
    private val _commandBuffer = MutableSharedFlow<List<NavigationCommand>>(
        replay = 0,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val commandBuffer: Flow<List<NavigationCommand>> = _commandBuffer

    fun executeCommands(vararg commands: NavigationCommand) {
        _commandBuffer.tryEmit(commands.toList())
    }

    /**
     * Open new screen and add it to the screens chain.
     *
     * @param screen screen
     */
    fun navigateTo(screen: ScreenBundle) {
        executeCommands(NavigationCommand.Forward(screen))
    }

    /**
     * Clear all screens and open new one as root.
     *
     * @param screen screen
     */
    fun newRootScreen(screen: ScreenBundle) {
        executeCommands(NavigationCommand.BackTo(null), NavigationCommand.Replace(screen))
    }

    /**
     * Replace current screen.
     *
     * By replacing the screen, you alters the backstack,
     * so by going fragmentBack you will return to the previous screen
     * and not to the replaced one.
     *
     * @param screen screen
     */
    fun replaceScreen(screen: ScreenBundle) {
        executeCommands(NavigationCommand.Replace(screen))
    }

    /**
     * Return fragmentBack to the needed screen from the chain.
     *
     * Behavior in the case when no needed screens found depends on
     * the processing of the [NavigationCommand.BackTo] command in a [Navigator] implementation.
     *
     * @param screen screen
     */
    fun backTo(screen: Screen?) {
        executeCommands(NavigationCommand.BackTo(screen))
    }

    /**
     * Opens several screens inside single transaction.
     *
     * @param screens
     */
    fun newChain(vararg screens: ScreenBundle) {
        val commands = screens.map { NavigationCommand.Forward(it) }
        executeCommands(*commands.toTypedArray())
    }

    /**
     * Clear current stack and open several screens inside single transaction.
     *
     * @param screens
     */
    fun newRootChain(vararg screens: ScreenBundle) {
        val commands = screens.mapIndexed { index, screen ->
            if (index == 0) {
                NavigationCommand.Replace(screen)
            } else {
                NavigationCommand.Forward(screen)
            }
        }
        executeCommands(NavigationCommand.BackTo(null), *commands.toTypedArray())
    }

    /**
     * Remove all screens from the chain and exit.
     *
     * It's mostly used to finish the application or close a supplementary navigation chain.
     */
    fun finishChain() {
        executeCommands(NavigationCommand.BackTo(null), NavigationCommand.Back)
    }

    /**
     * Return to the previous screen in the chain.
     *
     * Behavior in the case when the current screen is the root depends on
     * the processing of the [NavigationCommand.Back] command in a [Navigator] implementation.
     */
    fun exit() {
        executeCommands(NavigationCommand.Back)
    }
}
