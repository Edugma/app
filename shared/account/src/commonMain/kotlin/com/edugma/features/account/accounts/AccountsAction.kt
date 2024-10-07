package com.edugma.features.account.accounts

sealed interface AccountsAction {
    data object OnBack : AccountsAction
    data object AddNewGroup : AccountsAction
    data class SelectAccount(
        val accountGroupId: String,
        val accountId: String,
    ) : AccountsAction
    data class DeleteAccountGroup(val accountGroupId: String) : AccountsAction
}
