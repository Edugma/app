package io.edugma.navigation.core.navigator

import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import io.edugma.navigation.core.router.NavigationCommand
import io.edugma.navigation.core.router.Router
import io.edugma.navigation.core.utils.getRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ComposeNavigator(
    val navHostController: NavHostController,
    val router: Router,
    private val onCantBack: () -> Unit,
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
            applyCommand(command)
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
        navHostController.navigate(command.screen.getRoute())
    }

    private fun replace(command: NavigationCommand.Replace) {
        navHostController.popBackStack()
        navHostController.navigate(command.screen.getRoute())
    }

    private fun back() {
        val result = navHostController.popBackStack()
        if (result.not()) {
            onCantBack()
        }
    }

    private fun backTo(command: NavigationCommand.BackTo) {
        if (command.destination == null) {
            navHostController.clearBackStack("")
        } else {
            navHostController.popBackStack(command.destination!!.getRoute(), false)
        }
    }
}
