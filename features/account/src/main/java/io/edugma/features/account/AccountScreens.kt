package io.edugma.features.account

import io.edugma.core.navigation.AccountScreens
import io.edugma.core.navigation.MainScreen
import io.edugma.features.account.marks.PerformanceScreen
import io.edugma.features.account.menu.MenuScreen
import io.edugma.features.account.payments.PaymentsScreen
import io.edugma.features.account.people.classmates.ClassmatesScreen
import io.edugma.features.account.people.students.StudentsScreen
import io.edugma.features.account.people.teachers.TeachersScreen
import io.edugma.features.account.personal.PersonalScreen
import io.edugma.features.account.web.WebScreen
import io.edugma.navigation.core.graph.screenModule

val accountScreens = screenModule {
    groupScreen(MainScreen.Account, AccountScreens.Menu) {
        screen(AccountScreens.Menu) { MenuScreen() }
        screen(AccountScreens.Payments) { PaymentsScreen() }
        screen(AccountScreens.Teachers) { TeachersScreen() }
        screen(AccountScreens.Classmates) { ClassmatesScreen() }
        screen(AccountScreens.Students) { StudentsScreen() }
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
