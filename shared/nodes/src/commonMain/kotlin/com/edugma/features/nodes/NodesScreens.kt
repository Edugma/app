package com.edugma.features.nodes

import com.edugma.core.navigation.nodes.NodesScreens
import com.edugma.features.nodes.main.NodesMainScreen
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen

fun NavGraphBuilder.nodesScreens() {
    composeScreen(NodesScreens.Main) {
        NodesMainScreen()
    }
}
