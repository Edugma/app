package com.edugma.features.account.accounts

import androidx.compose.runtime.Immutable

@Immutable
data class AccountsUiState(
    val accountGroups: List<AccountGroupUiModel> = emptyList(),
    val selectedAccountGroupId: String? = null,
    val selectedAccountId: String? = null,
)
