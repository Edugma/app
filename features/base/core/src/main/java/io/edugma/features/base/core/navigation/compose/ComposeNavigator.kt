package io.edugma.features.base.core.navigation.compose

import co.touchlab.kermit.Logger
import io.edugma.features.base.core.navigation.core.Command
import io.edugma.features.base.core.navigation.core.Router
import io.edugma.navigation.core.navigator.EdugmaNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ComposeNavigator(
    val edugmaNavigator: EdugmaNavigator,
    private val router: Router,
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

    private fun processCommand(commands: List<Command>) {
        for (command in commands) {
            applyCommand(command)
        }
    }

    private fun applyCommand(command: Command) {
        Logger.d("applyCommand: $command", tag = TAG)
        when (command) {
            is Command.Forward -> forward(command)
            is Command.Replace -> replace(command)
            is Command.BackTo -> backTo(command)
            is Command.Back -> back()
        }
    }

    private fun forward(command: Command.Forward) {
        edugmaNavigator.navigateTo(command.screen)
    }

    private fun replace(command: Command.Replace) {
        edugmaNavigator.back()
        edugmaNavigator.navigateTo(command.screen)
    }

    private fun back() {
        edugmaNavigator.back()
    }

    private fun backTo(command: Command.BackTo) {
        if (command.screen == null) {
            edugmaNavigator.backTo(null)
        } else {
            edugmaNavigator.backTo(command.screen)
        }
    }
}
