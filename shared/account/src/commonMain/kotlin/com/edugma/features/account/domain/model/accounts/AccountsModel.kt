package com.edugma.features.account.domain.model.accounts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountModel(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("avatar")
    val avatar: String?,
    @SerialName("accessToken")
    val accessToken: String?,
    @SerialName("refreshToken")
    val refreshToken: String?,
)

@Serializable
data class AccountsModel(
    @SerialName("default")
    val default: String? = null,
    @SerialName("accounts")
    val accounts: List<AccountModel>,
)
