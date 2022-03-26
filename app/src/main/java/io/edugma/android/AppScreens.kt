package io.edugma.android

import androidx.navigation.NavGraphBuilder
import io.edugma.features.account.accountScreens
import io.edugma.features.home.homeScreens
import io.edugma.features.misc.miscScreens
import io.edugma.features.schedule.scheduleScreens

fun NavGraphBuilder.appScreens() {
    homeScreens()
    scheduleScreens()
    accountScreens()
    miscScreens()
}