package io.edugma.features.nodes

import io.edugma.core.navigation.nodes.NodesScreens
import io.edugma.features.nodes.main.NodesMainScreen
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.graph.composeScreen

fun NavGraphBuilder.nodesScreens() {
    composeScreen(NodesScreens.Main) {
        NodesMainScreen()
    }
}
