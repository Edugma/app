package com.edugma.navigation.core.router

import kotlinx.coroutines.flow.Flow
import com.edugma.navigation.core.destination.Destination
import com.edugma.navigation.core.destination.DestinationBundle
import com.edugma.navigation.core.graph.NavGraph
import com.edugma.navigation.core.router.navOptions.NavOptionsBuilder

open class DefaultRouter : Router {

    private val commandBus = DefaultCommandBus<NavigationCommand>()
    override val commands: Flow<NavigationCommand> = commandBus.commands

    override fun navigateTo(
        destination: DestinationBundle<*>,
        canDuplicate: Boolean,
        ignoreLock: Boolean,
        optionsBuilder: NavOptionsBuilder.() -> Unit,
    ) {
        commandBus.onCommand(NavigationCommand.ToScreen(destination, canDuplicate, ignoreLock, optionsBuilder))
    }

    override fun navigateTo(graph: NavGraph, optionsBuilder: NavOptionsBuilder.() -> Unit) {
        commandBus.onCommand(NavigationCommand.ToGraph(graph, optionsBuilder))
    }

    override fun navigateTo(deeplink: String, optionsBuilder: NavOptionsBuilder.() -> Unit) {
        commandBus.onCommand(NavigationCommand.Deeplink(deeplink, optionsBuilder))
    }

    override fun back() {
        commandBus.onCommand(NavigationCommand.Back)
    }

    override fun onSignal(key: String, backStackEntryId: String, action: () -> Unit) {
        onResult<Boolean>(key, backStackEntryId) { action() }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> onResult(key: String, backStackEntryId: String, action: (T) -> Unit) {
        commandBus.onCommand(NavigationCommand.AddResultListener(key, backStackEntryId) { action(it as T) })
    }

    override fun sendSignal(key: String) {
        sendResult(key, Unit)
    }

    override fun <T : Any> sendResult(key: String, result: T) {
        commandBus.onCommand(NavigationCommand.SendResult(key, result, usePreviousBackStackEntry = true))
    }

    override fun backWithSignal(key: String) {
        commandBus.onCommand(NavigationCommand.Back)
        commandBus.onCommand(NavigationCommand.SendResult(key, Unit))
    }

    override fun <T : Any> backWithResult(key: String, result: T) {
        commandBus.onCommand(NavigationCommand.Back)
        commandBus.onCommand(NavigationCommand.SendResult(key, result))
    }

    override fun backTo(destination: Destination) {
        commandBus.onCommand(NavigationCommand.BackTo(destination))
    }

    override fun <T : Any> backToWithResult(destination: Destination, key: String, result: T) {
        commandBus.onCommand(NavigationCommand.BackTo(destination))
        commandBus.onCommand(NavigationCommand.SendResult(key, result, usePreviousBackStackEntry = false))
    }

    override fun backToWithSignal(destination: Destination, key: String) {
        commandBus.onCommand(NavigationCommand.BackTo(destination))
        commandBus.onCommand(NavigationCommand.SendResult(key, Unit))
    }

    override fun lock() {
        commandBus.onCommand(NavigationCommand.Lock)
    }
}
