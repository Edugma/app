package io.edugma.features.base.core.navigation.compose

import android.util.Log
import androidx.navigation.NavHostController
import io.edugma.features.base.core.navigation.core.Command
import io.edugma.features.base.core.navigation.core.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ComposeNavigator(
    val navController: NavHostController,
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
                    Log.e(TAG, "", e)
                }
            }
        }

        scope.launch {
            navController.currentBackStackEntryFlow.collect {
                Log.d(TAG, "CurrentBackStackEntry: ${it.destination.route}")
            }
        }
    }

    private fun processCommand(commands: List<Command>) {
        for (command in commands) {
            applyCommand(command)
        }
    }

    private fun applyCommand(command: Command) {
        Log.d(TAG, "applyCommand: $command")
        when (command) {
            is Command.Forward -> forward(command)
            is Command.Replace -> replace(command)
            is Command.BackTo -> backTo(command)
            is Command.Back -> back()
        }
    }

    private fun forward(command: Command.Forward) {
        navController.navigate(command.screen.getFullRoute())
    }

    private fun replace(command: Command.Replace) {
        navController.popBackStack()
        navController.navigate(command.screen.getFullRoute())
    }

    private fun back() {
        navController.navigateUp()
    }

    private fun backTo(command: Command.BackTo) {
        if (command.screen == null) {
            navController.backQueue.firstOrNull()?.let {
                navController.popBackStack(it.id, false)
            }
        } else {
            navController.popBackStack(command.screen.getFullRoute(), true)
        }
    }
}
