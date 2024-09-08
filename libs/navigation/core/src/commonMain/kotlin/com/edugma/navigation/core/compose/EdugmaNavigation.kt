package com.edugma.navigation.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.edugma.navigation.core.destination.Destination
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.navigator.ComposeNavigator
import com.edugma.navigation.core.utils.getRoute
import androidx.navigation.NavGraph as JetpackNavGraph
import androidx.navigation.NavGraphBuilder as JetpackNavGraphBuilder

@Composable
fun EdugmaNavigation(
    navigator: ComposeNavigator,
    start: Destination,
    builder: NavGraphBuilder.() -> Unit,
) {
    val navigationBackHandler = LocalNavigationBackHandler.current

    val graph = navigator.navController.rememberJetpackNavigationGraph(
        start = start,
        builder = builder,
    )

    BackHandler {
        val canNavigateBack = navigator.navController.popBackStack()
        if (!canNavigateBack) {
            navigationBackHandler.onCantBack()
        }
    }
    CompositionLocalProvider(LocalNavigationBackHandler provides navigationBackHandler) {
        NavHost(
            navController = navigator.navController,
            graph = graph,
        )
    }
}

@Composable
fun EdugmaTabNavigation(
    navigator: ComposeNavigator,
    start: Destination,
    builder: NavGraphBuilder.() -> Unit,
) {
    val navigationBackHandler = remember {
        NavigationBackHandler(
            onCantBack = {
                navigator.navController.popBackStack()
            },
        )
    }

    val graph = navigator.navController.rememberJetpackNavigationGraph(
        start = start,
        builder = builder,
    )

    CompositionLocalProvider(LocalNavigationBackHandler provides navigationBackHandler) {
        NavHost(
            navController = navigator.navController,
            graph = graph,
            //enterTransition = { EnterTransition.None },
            //exitTransition = { ExitTransition.None },
        )
    }
}

@Composable
public fun NavController.rememberJetpackNavigationGraph(
    start: Destination,
    builder: NavGraphBuilder.() -> Unit,
): JetpackNavGraph {
    return remember(this.navigatorProvider, start, builder) {
        val jetpackBuilder = JetpackNavGraphBuilder(
            provider = this@rememberJetpackNavigationGraph.navigatorProvider,
            startDestination = start.getRoute(),
            route = null,
        )
        builder(NavGraphBuilder(jetpackBuilder))
        jetpackBuilder.build()
    }
}
