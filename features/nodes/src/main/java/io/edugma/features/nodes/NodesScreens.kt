package io.edugma.features.nodes

import androidx.navigation.NavGraphBuilder
import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.navigation.nodes.NodesScreens
import io.edugma.features.nodes.main.NodesMainScreen

fun NavGraphBuilder.nodesScreens() {
    addScreen<NodesScreens.Main> {
        NodesMainScreen()
    }
}