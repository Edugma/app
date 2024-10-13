package com.edugma.features.account.accounts

import androidx.compose.runtime.Immutable
import com.edugma.features.account.domain.model.accounts.AccountGroupModel

@Immutable
data class AccountGroupUiModel(
    val id: String,
    val selected: String?,
    val accounts: List<AccountUiModel>,
)

fun AccountGroupModel.toUiModel(): AccountGroupUiModel {
    return AccountGroupUiModel(
        id = id,
        selected = selected,
        accounts = accounts.map { it.toUiModel() },
    )
}
