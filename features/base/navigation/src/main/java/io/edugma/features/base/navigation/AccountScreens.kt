package io.edugma.features.base.navigation

import io.edugma.features.base.core.navigation.core.Screen

object AccountScreens {

    private const val prefix = "account"

    object Menu : Screen() // "$prefix-menu")

    object Applications : Screen() // "$prefix-applications")

    object Personal : Screen() // "$prefix-personal")

    object Marks : Screen() // "$prefix-marks")

    object Students : Screen() // "$prefix-students")

    object Classmates : Screen() // "$prefix-classmates")

    object Teachers : Screen() // "$prefix-teachers")

    object Payments : Screen() // "$prefix-payments")

    data class Web(
        val url: String,
        val isFullScreen: Boolean = false,
    ) : Screen(Web::url.name to url, Web::isFullScreen.name to isFullScreen) // "$prefix-web")
}
