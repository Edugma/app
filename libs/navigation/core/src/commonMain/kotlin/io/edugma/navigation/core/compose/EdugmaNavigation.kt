package io.edugma.navigation.core.compose

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder as JetpackNavGraphBuilder
import androidx.navigation.NavGraph as JetpackNavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import io.edugma.navigation.core.graph.NavGraph
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.navigator.ComposeNavigator
import io.edugma.navigation.core.screen.Destination
import io.edugma.navigation.core.screen.DestinationBundle
import io.edugma.navigation.core.utils.getRoute

@Composable
fun EdugmaNavigation(
    navigator: ComposeNavigator,
    start: Destination,
    builder: NavGraphBuilder.() -> Unit,
) {
    val navigationBackHandler = LocalNavigationBackHandler.current

    val graph = navigator.navHostController.rememberJetpackNavigationGraph(
        start = start,
        builder = builder,
    )

    BackHandler {
        val canNavigateBack = navigator.navHostController.popBackStack()
        if (!canNavigateBack) {
            navigationBackHandler.onCantBack()
        }
    }
    CompositionLocalProvider(LocalNavigationBackHandler provides navigationBackHandler) {
        NavHost(
            navController = navigator.navHostController,
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
                navigator.navHostController.popBackStack()
            },
        )
    }

    val graph = navigator.navHostController.rememberJetpackNavigationGraph(
        start = start,
        builder = builder,
    )

    CompositionLocalProvider(LocalNavigationBackHandler provides navigationBackHandler) {
        NavHost(
            navController = navigator.navHostController,
            graph = graph,
        )
    }
}

@Composable
public fun NavController.rememberJetpackNavigationGraph(
    start: Destination,
    builder: NavGraphBuilder.() -> Unit,
): JetpackNavGraph {
    return remember(start, builder) {
        val jetpackBuilder = JetpackNavGraphBuilder(
            provider = this@rememberJetpackNavigationGraph.navigatorProvider,
            startDestination = start.getRoute(),
            route = null,
        )
        builder(NavGraphBuilder(jetpackBuilder))
        jetpackBuilder.build()
    }
}
