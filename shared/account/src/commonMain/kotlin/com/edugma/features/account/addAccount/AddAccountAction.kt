package com.edugma.features.account.addAccount

sealed interface AddAccountAction {
    data object OnBack : AddAccountAction
    data object OnLoginClick : AddAccountAction
    data class LoginEnter(val login: String) : AddAccountAction
    data class PasswordEnter(val password: String) : AddAccountAction
    data class SetPasswordVisible(val isVisible: Boolean) : AddAccountAction
}
