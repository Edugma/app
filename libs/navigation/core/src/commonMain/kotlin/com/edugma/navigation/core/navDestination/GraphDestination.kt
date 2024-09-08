package com.edugma.navigation.core.navDestination

import com.edugma.navigation.core.destination.Destination
import com.edugma.navigation.core.graph.NavGraph
import com.edugma.navigation.core.graph.NavGraphBuilder

class GraphDestination(
    public val graph: NavGraph,
    public val startDestination: Destination?,
    public val startGraph: NavGraph?,
    public val graphBuilder: NavGraphBuilder.() -> Unit,
) : NavDestination
