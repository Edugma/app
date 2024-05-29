package com.edugma.navigation.core.graph

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.core.bundle.bundleOf
import androidx.navigation.compose.composable
//import com.edugma.navigation.core.bottomSheet.bottomSheet
import com.edugma.navigation.core.compose.ComposeArgumentsStore
import com.edugma.navigation.core.compose.LocalArguments
import com.edugma.navigation.core.navDestination.ComposeDestination
import com.edugma.navigation.core.navDestination.ComposeDialogDestination
import com.edugma.navigation.core.navDestination.GraphDestination
import com.edugma.navigation.core.navDestination.NavDestination
import com.edugma.navigation.core.utils.getNamedNavArgument
import com.edugma.navigation.core.utils.getRoute
import androidx.navigation.NavGraphBuilder as JetpackNavGraphBuilder

class NavGraphBuilder(
    private val navGraphBuilder: JetpackNavGraphBuilder,
) {
    fun addDestination(destination: NavDestination) {
        when (destination) {
            is GraphDestination -> navGraphBuilder.graph(destination)
            is ComposeDestination -> navGraphBuilder.screen(destination)
            is ComposeDialogDestination -> navGraphBuilder.dialog(destination)
        }
    }

    private fun JetpackNavGraphBuilder.graph(graphDestination: GraphDestination) {
        val startDestination = graphDestination.startDestination?.getRoute()
            ?: graphDestination.startGraph?.getRoute()
        checkNotNull(startDestination)
        destination(
            JetpackNavGraphBuilder(
                provider = provider,
                startDestination = startDestination,
                route = graphDestination.graph.getRoute(),
            ).apply {
                graphDestination.graphBuilder(NavGraphBuilder(this))
            },
        )
    }

    private fun JetpackNavGraphBuilder.screen(destination: ComposeDestination) {
        composable(
            route = destination.destination.getRoute(),
            arguments = destination.destination.getArgs().map { getNamedNavArgument(it) },
            //deepLinks = destination.destination.getDeepLinks().map { getDeeplink(it) },
            enterTransition = destination.enterTransition,
            exitTransition = destination.exitTransition,
            popEnterTransition = destination.popEnterTransition,
            popExitTransition = destination.popExitTransition,
            content = {
                CompositionLocalProvider(
                    LocalArguments provides ComposeArgumentsStore(it.arguments ?: bundleOf()),
                    //LocalNavBackStackEntry provides JetpackNavBackStackEntry(it),
                ) {
                    destination.composeScreen()
                }
            },
        )
    }

    private fun JetpackNavGraphBuilder.dialog(destination: ComposeDialogDestination) {
//        bottomSheet(
//            route = destination.destination.getRoute(),
//            arguments = destination.destination.getArgs().map { getNamedNavArgument(it) },
//            //deepLinks = destination.destination.getDeepLinks().map { getDeeplink(it) },
//        ) {
//            CompositionLocalProvider(
//                LocalArguments provides ComposeArgumentsStore(it.arguments ?: bundleOf()),
//                //LocalNavBackStackEntry provides JetpackNavBackStackEntry(it),
//            ) {
//                destination.composeScreen()
//            }
//        }
    }
}

