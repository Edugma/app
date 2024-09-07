package com.edugma.navigation.core.navigator

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import co.touchlab.kermit.Logger
import com.edugma.navigation.core.router.NavigationCommand
import com.edugma.navigation.core.router.Router
import com.edugma.navigation.core.router.navOptions.NavPopUpToBuilder
import com.edugma.navigation.core.utils.getRoute
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

private const val RESULT_PREFIX = "_res_"
private const val CONSUMED_POSTFIX = "_consumed"
private typealias EdNavOptionsBuilder = com.edugma.navigation.core.router.navOptions.NavOptionsBuilder
private const val TAG = "ComposeNavigator"

class ComposeNavigator(
    val navController: NavHostController,
    val router: Router,
    private val onCantBack: () -> Unit,
) {
    suspend fun listenCommands() {
        router.commands.takeWhile { it !is NavigationCommand.Lock }.collect {
            processCommand(it)
            //printBackStack()
        }
    }

    private fun printBackStack() {
        val backStack = navController.currentBackStack.value
        val list = backStack.joinToString(separator = "\n") { it.destination.route.orEmpty() }
        Logger.i("New back stack: $list", tag = TAG)
    }

    private fun processCommand(command: NavigationCommand) {
        when (command) {
            is NavigationCommand.ToScreen -> navigate(command)

            is NavigationCommand.ToGraph -> navigate(command)

            is NavigationCommand.Deeplink -> navigateToDeeplink(command)

            is NavigationCommand.Back -> back()

            is NavigationCommand.BackTo -> backTo(command)

            is NavigationCommand.SendResult<*> -> setResult(
                command.key,
                command.result,
                command.usePreviousBackStackEntry,
            )

            is NavigationCommand.AddResultListener -> addResultListener(
                key = command.key,
                backStackEntryId = command.backStackEntryId,
                action = command.action,
            )

            NavigationCommand.Lock -> {}
        }
    }

    private fun back() {
        val backSuccess = navController.popBackStack()
        if (backSuccess.not()) {
            onCantBack()
        }
    }

    private fun navigate(command: NavigationCommand.ToScreen) {
        val currentDestination = navController.currentDestination

        if (command.canDuplicate || currentDestination?.route != command.destinationBundle.destination.getRoute()) {
            navController.navigate(command.destinationBundle.getRoute()) {
                navOptions(EdNavOptionsBuilder().apply(command.optionsBuilder))
            }
        }
    }

    private fun navigate(command: NavigationCommand.ToGraph) {
        navController.navigate(command.graph.getRoute()) {
            navOptions(EdNavOptionsBuilder().apply(command.optionsBuilder))
        }
    }

    private fun navigateToDeeplink(command: NavigationCommand.Deeplink) {
//        navController.navigate(
//            command.deeplink.toUri(),
//            navOptions {
//                navOptions(OzonNavOptionsBuilder().apply(command.optionsBuilder))
//            },
//        )
    }

    private fun backTo(command: NavigationCommand.BackTo) {
        navController.popBackStack(command.destination.getRoute(), false)
    }


    private fun NavOptionsBuilder.navOptions(navOptions: EdNavOptionsBuilder) {
        launchSingleTop = navOptions.launchSingleTop
        restoreState = navOptions.restoreState
        navOptions.popUpTo?.let { navPopUpTo(it) }
    }

    private fun NavOptionsBuilder.navPopUpTo(navPopUpTo: NavPopUpToBuilder) {
        popUpTo(navPopUpTo.destination.getRoute()) {
            inclusive = navPopUpTo.inclusive
            saveState = navPopUpTo.saveState
        }
    }


    private fun addResultListener(
        key: String,
        backStackEntryId: String,
        action: (Any) -> Unit,
    ) {
        val scope: LifecycleCoroutineScope
        val savedStateHandle: SavedStateHandle

        val backStackEntry = navController.currentBackStack.value
            .lastOrNull { entry -> entry.id == backStackEntryId }

        val backStackEntry2 = navController.currentBackStackEntry

        if (backStackEntry != null) {
            scope = backStackEntry.lifecycleScope
            savedStateHandle = backStackEntry.savedStateHandle
        } else if (backStackEntry2 != null) {
            // Нежелательный вариант. Выпилить при отказе от onResult и onSignal без передачи backStackEntry
            scope = backStackEntry2.lifecycleScope
            savedStateHandle = backStackEntry2.savedStateHandle
        } else {
            return
        }

        val resultKey = RESULT_PREFIX + key
        val consumedKey = resultKey + CONSUMED_POSTFIX

        scope.launch {
            combine(
                savedStateHandle.getStateFlow<Any?>(resultKey, null),
                savedStateHandle.getStateFlow<Boolean?>(consumedKey, true)
            ) { result, consumed ->
                if (result != null && consumed == false) {
                    try {
                        action.invoke(result)
                        savedStateHandle.set(consumedKey, true)
                    } catch (e: Exception) {
                        Logger.e("NavigationArgument", e)
                    }
                }
            }.collect()
        }
    }

    private fun setResult(key: String, result: Any, usePreviousBackStackEntry: Boolean) {
        when (result) {
            !is Unit, Int, Long, String, Boolean -> {
                Logger.e("Результат должен быть примитивным типом")
                return
            }
        }

        val resultKey = RESULT_PREFIX + key
        val consumedKey = resultKey + CONSUMED_POSTFIX

        val backStackEntry = if (usePreviousBackStackEntry) {
            navController.previousBackStackEntry
        } else {
            navController.currentBackStackEntry
        }

        backStackEntry?.savedStateHandle?.set(resultKey, result)
        backStackEntry?.savedStateHandle?.set(consumedKey, false)
    }
}
