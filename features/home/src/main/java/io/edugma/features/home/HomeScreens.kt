package io.edugma.features.home

import androidx.navigation.NavGraphBuilder
import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.groupScreen
import io.edugma.features.base.navigation.HomeScreens
import io.edugma.features.base.navigation.MainScreen

fun NavGraphBuilder.homeScreens() {
    groupScreen<MainScreen.Home, HomeScreens.Main> {
        addScreen<HomeScreens.Main> { HomeScreen() }
    }
}