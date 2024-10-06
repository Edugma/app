package com.edugma.features.account.domain.model.accounts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountGroupModel(
    @SerialName("id")
    val id: String,
    @SerialName("default")
    val default: String?,
    @SerialName("selected")
    val selected: String?,
    @SerialName("accounts")
    val accounts: List<AccountModel>,
)
