package com.edugma.navigation.core.router

import com.edugma.navigation.core.destination.Destination
import com.edugma.navigation.core.destination.DestinationBundle
import com.edugma.navigation.core.graph.NavGraph
import com.edugma.navigation.core.router.navOptions.NavOptionsBuilder

sealed interface NavigationCommand {

    data object Back : NavigationCommand

    data class BackTo(val destination: Destination) : NavigationCommand

    data class Deeplink(
        val deeplink: String,
        val optionsBuilder: NavOptionsBuilder.() -> Unit = { },
    ) : NavigationCommand

    data class SendResult<T : Any>(
        val key: String,
        val result: T,
        val usePreviousBackStackEntry: Boolean = false,
    ) : NavigationCommand

    data class AddResultListener(
        val key: String,
        val backStackEntryId: String,
        val action: (Any) -> Unit,
    ) : NavigationCommand

    data class ToScreen(
        val destinationBundle: DestinationBundle<*>,
        val canDuplicate: Boolean,
        val ignoreLock: Boolean = false,
        val optionsBuilder: NavOptionsBuilder.() -> Unit = { },
    ) : NavigationCommand

    data class ToGraph(
        val graph: NavGraph,
        val optionsBuilder: NavOptionsBuilder.() -> Unit = { },
    ) : NavigationCommand

    data object Lock : NavigationCommand
}
