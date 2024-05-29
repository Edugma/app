package com.edugma.features.home

import com.edugma.core.navigation.HomeScreens
import com.edugma.core.navigation.MainDestination
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen
import com.edugma.navigation.core.graph.graph

fun NavGraphBuilder.homeScreens() {
    graph(MainDestination.Home, HomeScreens.Main) {
        composeScreen(HomeScreens.Main) { HomeScreen() }
    }
}
