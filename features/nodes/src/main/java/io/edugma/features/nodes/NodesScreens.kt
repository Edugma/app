package io.edugma.features.nodes

import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.nodes.NodesScreens
import io.edugma.features.nodes.main.NodesMainScreen

val nodesScreens = screens {
    addScreen<NodesScreens.Main> {
        NodesMainScreen()
    }
}
