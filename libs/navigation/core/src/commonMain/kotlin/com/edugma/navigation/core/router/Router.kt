package com.edugma.navigation.core.router

import kotlinx.coroutines.flow.Flow
import com.edugma.navigation.core.destination.Destination
import com.edugma.navigation.core.destination.DestinationBundle
import com.edugma.navigation.core.graph.NavGraph
import com.edugma.navigation.core.router.navOptions.NavOptionsBuilder

interface Router {
    val commands: Flow<NavigationCommand>

    fun navigateTo(
        destination: DestinationBundle<*>,
        canDuplicate: Boolean = false,
        ignoreLock: Boolean = false,
        optionsBuilder: NavOptionsBuilder.() -> Unit = { },
    )

    fun navigateTo(
        graph: NavGraph,
        optionsBuilder: NavOptionsBuilder.() -> Unit = { },
    )

    fun navigateTo(
        deeplink: String,
        optionsBuilder: NavOptionsBuilder.() -> Unit = { },
    )

    fun back()


    fun onSignal(key: String, backStackEntryId: String, action: () -> Unit)


    fun <T : Any> onResult(key: String, backStackEntryId: String, action: (T) -> Unit)

    fun sendSignal(key: String)

    fun <T : Any> sendResult(key: String, result: T)

    fun backWithSignal(key: String)

    fun <T : Any> backWithResult(key: String, result: T)

    fun backTo(destination: Destination)

    fun <T : Any> backToWithResult(destination: Destination, key: String, result: T)

    fun backToWithSignal(destination: Destination, key: String)

    fun lock()
}
