package io.edugma.navigation.core.graph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import io.edugma.navigation.core.navDestination.ComposeDestination
import io.edugma.navigation.core.navDestination.ComposeDialogDestination
import io.edugma.navigation.core.navDestination.GraphDestination
import io.edugma.navigation.core.destination.Destination
import kotlin.jvm.JvmSuppressWildcards

fun NavGraphBuilder.composeScreen(
    destination: Destination,
    enterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? =
        enterTransition,
    popExitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? =
        exitTransition,
    composeScreen: @Composable () -> Unit,
) {
    addDestination(ComposeDestination(destination, composeScreen, enterTransition, exitTransition, popEnterTransition, popExitTransition))
}

fun NavGraphBuilder.composeDialog(
    destination: Destination,
    composeScreen: @Composable () -> Unit,
) {
    addDestination(
        ComposeDialogDestination(destination, composeScreen),
    )
}

fun NavGraphBuilder.graph(
    graph: NavGraph,
    startDestination: Destination,
    builder: NavGraphBuilder.() -> Unit,
) {
    addDestination(
        GraphDestination(
            graph = graph,
            startDestination = startDestination,
            startGraph = null,
            graphBuilder = builder,
        ),
    )
}

