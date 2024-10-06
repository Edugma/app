package com.edugma.features.account.accounts

import androidx.compose.runtime.Immutable
import com.edugma.features.account.domain.model.accounts.AccountGroupModel

@Immutable
data class AccountsUiState(
    val accountGroups: List<AccountGroupModel> = emptyList(),
    val selectedAccountGroupId: String? = null,
    val selectedAccountId: String? = null,
)
