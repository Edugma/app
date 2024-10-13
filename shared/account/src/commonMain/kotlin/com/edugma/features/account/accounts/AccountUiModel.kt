package com.edugma.features.account.accounts

import androidx.compose.runtime.Immutable
import com.edugma.features.account.domain.model.accounts.AccountModel

@Immutable
data class AccountUiModel(
    val id: String,
    val name: String,
    val description: String,
    val avatar: String?,
)

fun AccountModel.toUiModel(): AccountUiModel {
    return AccountUiModel(
        id = id,
        name = name,
        description = description,
        avatar = avatar,
    )
}
