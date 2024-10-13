package com.edugma.features.misc.aboutApp

import com.edugma.core.navigation.misc.MiscMenuScreens
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen

fun NavGraphBuilder.aboutAppScreens() {
    composeScreen(MiscMenuScreens.AboutApp) {
        AboutAppScreen()
    }
}
