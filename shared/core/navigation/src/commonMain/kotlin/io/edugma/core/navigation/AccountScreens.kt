package io.edugma.core.navigation

import io.edugma.navigation.core.screen.NoArgScreen
import io.edugma.navigation.core.screen.Screen
import io.edugma.navigation.core.screen.bundleOf
import io.edugma.navigation.core.screen.set

object AccountScreens {

    object Menu : NoArgScreen("accountMenu")

    object Applications : NoArgScreen("accountApplications")

    object Personal : NoArgScreen("accountPersonal")

    object Marks : NoArgScreen("accountMarks")

    object Students : NoArgScreen("accountStudents")

    object Classmates : NoArgScreen("accountClassmates")

    object Teachers : NoArgScreen("accountTeachers")

    object Payments : NoArgScreen("accountPayments")

    object Web : Screen("accountWeb") {
        val url = reqArg<String>("url")
        val isFullScreen = optArg("isFullScreen", false)

        operator fun invoke(
            url: String,
            isFullScreen: Boolean? = null,
        ) = bundleOf(
            this.url set url,
            this.isFullScreen set isFullScreen,
        )
    }
}
