package com.edugma.features.account.accounts

import androidx.compose.runtime.Immutable
import com.edugma.core.api.model.LceUiState

@Immutable
data class AccountsUiState(
    val lceState: LceUiState = LceUiState.init(),
    val accountGroups: List<AccountGroupUiModel> = emptyList(),
    val selectedAccountGroupId: String? = null,
    val selectedAccountId: String? = null,
) {
    fun toContent(accountGroups: List<AccountGroupUiModel>) =
        copy(
            accountGroups = accountGroups,
            lceState = lceState.toContent(accountGroups.isEmpty())
        )
}
