package io.edugma.features.account

import io.edugma.core.navigation.AccountScreens
import io.edugma.core.navigation.MainDestination
import io.edugma.features.account.menu.MenuScreen
import io.edugma.features.account.payments.PaymentsScreen
import io.edugma.features.account.people.PeopleScreenType
import io.edugma.features.account.people.presentation.PeopleScreen
import io.edugma.features.account.performance.PerformanceScreen
import io.edugma.features.account.personal.PersonalScreen
import io.edugma.features.account.web.WebScreen
import io.edugma.navigation.core.compose.rememberNavArgs
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.graph.composeScreen
import io.edugma.navigation.core.graph.graph

fun NavGraphBuilder.accountScreens() {
    graph(MainDestination.Account, AccountScreens.Menu) {
        composeScreen(AccountScreens.Menu) { MenuScreen() }
        composeScreen(AccountScreens.Payments) { PaymentsScreen() }
        composeScreen(AccountScreens.Teachers) {
            PeopleScreen(type = PeopleScreenType.teachers())
        }
        composeScreen(AccountScreens.Classmates) {
            PeopleScreen(type = PeopleScreenType.classmates())
        }
        composeScreen(AccountScreens.Students) {
            PeopleScreen(type = PeopleScreenType.students())
        }
        composeScreen(AccountScreens.Marks) { PerformanceScreen() }
        composeScreen(AccountScreens.Personal) { PersonalScreen() }
        composeScreen(AccountScreens.Web) {
            val args = rememberNavArgs(AccountScreens.Web)
            WebScreen(
                args { destination.url.get() },
                args { destination.isFullScreen.get() },
            )
        }
    }
}
