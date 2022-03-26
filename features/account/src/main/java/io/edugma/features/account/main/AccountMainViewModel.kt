package io.edugma.features.account.main

import io.edugma.features.account.main.model.MenuUi
import io.edugma.features.account.main.model.MenuUi.*
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.features.base.core.mvi.BaseViewModelFull
import io.edugma.features.base.navigation.AccountScreens

class AccountMainViewModel :
    BaseViewModelFull<AccountMenuState, AccountMenuMutator, Nothing>(AccountMenuState(), ::AccountMenuMutator) {

    fun navigateToAuth() {
        router.navigateTo(AccountScreens.Authorization)
    }

    fun navigateToApplications() {
        router.navigateTo(AccountScreens.Applications)
    }

    fun navigateToPayments() {
        router.navigateTo(AccountScreens.Payments)
    }

    fun navigateToStudents() {
        router.navigateTo(AccountScreens.Students)
    }

    fun navigateToTeachers() {
        router.navigateTo(AccountScreens.Teachers)
    }

    fun navigateToClassmates() {
        router.navigateTo(AccountScreens.Classmates)
    }

    fun navigateToMarks() {
        router.navigateTo(AccountScreens.Marks)
    }

    fun navigateToPersonal() {
        router.navigateTo(AccountScreens.Personal)
    }
}

data class AccountMenuState(
    val menu: List<MenuUi> = listOf(Auth,
        Personal, Students, Teachers, Classmates, Payments, Applications, Marks)
)

class AccountMenuMutator : BaseMutator<AccountMenuState>() {
    fun setMenu(menu: List<MenuUi>) {
        if (state.menu != menu) {
            state = state.copy(menu = menu)
        }
    }
}
