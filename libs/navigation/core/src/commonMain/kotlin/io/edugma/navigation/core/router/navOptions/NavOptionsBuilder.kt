package io.edugma.navigation.core.router.navOptions

import io.edugma.navigation.core.destination.Destination

class NavOptionsBuilder {
    var popUpTo: NavPopUpToBuilder? = null
    var launchSingleTop: Boolean = false
    var restoreState: Boolean = false

    inline fun popUpTo(destination: Destination, builder: NavPopUpToBuilder.() -> Unit = { }) {
        popUpTo = NavPopUpToBuilder(destination).apply(builder)
    }
}
