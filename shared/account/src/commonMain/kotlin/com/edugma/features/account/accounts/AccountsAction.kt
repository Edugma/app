package com.edugma.features.account.accounts

sealed interface AccountsAction {
    data object OnBack : AccountsAction
}
