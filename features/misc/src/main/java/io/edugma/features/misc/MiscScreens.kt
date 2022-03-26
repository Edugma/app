package io.edugma.features.misc

import androidx.navigation.NavGraphBuilder
import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.groupScreen
import io.edugma.features.base.navigation.MiscScreens
import io.edugma.features.base.navigation.MainScreen

fun NavGraphBuilder.miscScreens() {
    groupScreen<MainScreen.Misc, MiscScreens.Menu> {
        addScreen<MiscScreens.Menu> { MiscMenuScreen() }
    }
}