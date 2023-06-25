package io.edugma.core.navigation.core

import co.touchlab.kermit.Logger
import io.edugma.navigation.core.navigator.EdugmaNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ComposeNavigator(
    val edugmaNavigator: EdugmaNavigator,
    private val router: Router,
    private val isActive: () -> Boolean,
) {
    companion object {
        private const val TAG = "ComposeNavigator"
    }

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    init {
        scope.launch {
            router.commandBuffer.collect {
                try {
                    processCommand(it)
                } catch (e: Exception) {
                    Logger.e("", e, tag = TAG)
                }
            }
        }

//        scope.launch {
//            navController.currentScreen.collect {
//                Logger.d("CurrentBackStackEntry: ${it.destination.route}", tag = TAG)
//            }
//        }
    }

    private fun processCommand(commands: List<NavigationCommand>) {
        for (command in commands) {
            if (isActive()) {
                applyCommand(command)
            }
        }
    }

    private fun applyCommand(command: NavigationCommand) {
        Logger.d("applyCommand: $command", tag = TAG)
        when (command) {
            is NavigationCommand.Forward -> forward(command)
            is NavigationCommand.Replace -> replace(command)
            is NavigationCommand.BackTo -> backTo(command)
            is NavigationCommand.Back -> back()
        }
    }

    private fun forward(command: NavigationCommand.Forward) {
        edugmaNavigator.navigateTo(command.screen)
    }

    private fun replace(command: NavigationCommand.Replace) {
        edugmaNavigator.back()
        edugmaNavigator.navigateTo(command.screen)
    }

    private fun back() {
        edugmaNavigator.back()
    }

    private fun backTo(command: NavigationCommand.BackTo) {
        if (command.screen == null) {
            edugmaNavigator.backTo(null)
        } else {
            edugmaNavigator.backTo(command.screen)
        }
    }
}
