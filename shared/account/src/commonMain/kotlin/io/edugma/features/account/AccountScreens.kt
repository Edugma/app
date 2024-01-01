package io.edugma.features.account

import io.edugma.core.navigation.AccountScreens
import io.edugma.core.navigation.MainScreen
import io.edugma.features.account.menu.MenuScreen
import io.edugma.features.account.payments.PaymentsScreen
import io.edugma.features.account.people.PeopleScreenType
import io.edugma.features.account.people.presentation.PeopleScreen
import io.edugma.features.account.performance.PerformanceScreen
import io.edugma.features.account.personal.PersonalScreen
import io.edugma.features.account.web.WebScreen
import io.edugma.navigation.core.graph.screenModule

val accountScreens = screenModule {
    groupScreen(MainScreen.Account, AccountScreens.Menu) {
        screen(AccountScreens.Menu) { MenuScreen() }
        screen(AccountScreens.Payments) { PaymentsScreen() }
        screen(AccountScreens.Teachers) {
            PeopleScreen(type = PeopleScreenType.teachers())
        }
        screen(AccountScreens.Classmates) {
            PeopleScreen(type = PeopleScreenType.classmates())
        }
        screen(AccountScreens.Students) {
            PeopleScreen(type = PeopleScreenType.students())
        }
        screen(AccountScreens.Marks) { PerformanceScreen() }
        screen(AccountScreens.Personal) { PersonalScreen() }
        screen(AccountScreens.Web) {
            WebScreen(
                screen.url.get(),
                screen.isFullScreen.get(),
            )
        }
    }
}
