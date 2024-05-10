package io.edugma.navigation.core.navDestination

import io.edugma.navigation.core.graph.NavGraph
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.screen.Destination

class GraphDestination(
    public val graph: NavGraph,
    public val startDestination: Destination?,
    public val startGraph: NavGraph?,
    public val graphBuilder: NavGraphBuilder.() -> Unit,
) : NavDestination

