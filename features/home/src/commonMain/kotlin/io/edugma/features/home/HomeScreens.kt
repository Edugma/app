package io.edugma.features.home

import io.edugma.core.navigation.HomeScreens
import io.edugma.core.navigation.MainScreen
import io.edugma.navigation.core.graph.screenModule

val homeScreens = screenModule {
    groupScreen(MainScreen.Home, HomeScreens.Main) {
        screen(HomeScreens.Main) { HomeScreen() }
    }
}
