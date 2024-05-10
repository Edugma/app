package io.edugma.core.navigation

import io.edugma.navigation.core.screen.NoArgDestination
import io.edugma.navigation.core.screen.Destination
import io.edugma.navigation.core.screen.optArg
import io.edugma.navigation.core.screen.reqArg
import io.edugma.navigation.core.screen.toBundle

object AccountScreens {

    object Menu : NoArgDestination("accountMenu")

    object Applications : NoArgDestination("accountApplications")

    object Personal : NoArgDestination("accountPersonal")

    object Marks : NoArgDestination("accountMarks")

    object Students : NoArgDestination("accountStudents")

    object Classmates : NoArgDestination("accountClassmates")

    object Teachers : NoArgDestination("accountTeachers")

    object Payments : NoArgDestination("accountPayments")

    object Web : Destination("accountWeb") {
        val url = reqArg<String>("url")
        val isFullScreen = optArg("isFullScreen", false)

        operator fun invoke(
            url: String,
            isFullScreen: Boolean? = null,
        ) = toBundle {
            destination.url set url
            destination.isFullScreen set isFullScreen
        }
    }
}
