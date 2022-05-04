package io.edugma.domain.account.model

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val login: String,
    val password: String
)
