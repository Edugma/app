package io.edugma.features.home

import io.edugma.core.navigation.HomeScreens
import io.edugma.core.navigation.MainDestination
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.graph.composeScreen
import io.edugma.navigation.core.graph.graph

fun NavGraphBuilder.homeScreens() {
    graph(MainDestination.Home, HomeScreens.Main) {
        composeScreen(HomeScreens.Main) { HomeScreen() }
    }
}
