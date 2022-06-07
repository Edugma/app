package io.edugma.features.account

import androidx.navigation.NavGraphBuilder
import io.edugma.features.account.authorization.AuthScreen
import io.edugma.features.account.classmates.ClassmatesScreen
import io.edugma.features.account.main.AccountMainScreen
import io.edugma.features.account.marks.PerformanceScreen
import io.edugma.features.account.payments.PaymentsScreen
import io.edugma.features.account.personal.PersonalScreen
import io.edugma.features.account.students.StudentsScreen
import io.edugma.features.account.teachers.TeachersScreen
import io.edugma.features.base.core.navigation.compose.addScreen
import io.edugma.features.base.core.navigation.compose.groupScreen
import io.edugma.features.base.navigation.AccountScreens
import io.edugma.features.base.navigation.MainScreen

fun NavGraphBuilder.accountScreens() {
    groupScreen<MainScreen.Account, AccountScreens.Menu> {
        addScreen<AccountScreens.Menu> { AccountMainScreen() }
        addScreen<AccountScreens.Authorization> { AuthScreen() }
        addScreen<AccountScreens.Payments> { PaymentsScreen() }
        addScreen<AccountScreens.Teachers> { TeachersScreen() }
        addScreen<AccountScreens.Classmates> { ClassmatesScreen() }
        addScreen<AccountScreens.Students> { StudentsScreen() }
        addScreen<AccountScreens.Marks> { PerformanceScreen() }
        addScreen<AccountScreens.Personal> { PersonalScreen() }
    }
}
