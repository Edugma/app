package com.edugma.features.account

import com.edugma.core.navigation.AccountScreens
import com.edugma.core.navigation.MainDestination
import com.edugma.features.account.accounts.AccountsScreen
import com.edugma.features.account.menu.MenuScreen
import com.edugma.features.account.payments.PaymentsScreen
import com.edugma.features.account.people.PeopleScreenType
import com.edugma.features.account.people.presentation.PeopleScreen
import com.edugma.features.account.performance.PerformanceScreen
import com.edugma.features.account.personal.PersonalScreen
import com.edugma.features.account.web.WebScreen
import com.edugma.navigation.core.compose.rememberNavArgs
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.composeScreen
import com.edugma.navigation.core.graph.graph

fun NavGraphBuilder.accountScreens() {
    graph(MainDestination.Account, AccountScreens.Menu) {
        composeScreen(AccountScreens.Menu) { MenuScreen() }
        composeScreen(AccountScreens.Accounts) { AccountsScreen() }
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
