package io.edugma.features.nodes

import io.edugma.core.navigation.nodes.NodesScreens
import io.edugma.features.nodes.main.NodesMainScreen
import io.edugma.navigation.core.graph.screenModule

val nodesScreens = screenModule {
    screen(NodesScreens.Main) {
        NodesMainScreen()
    }
}
