package io.edugma.features.account.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val login: String,
    val password: String,
)
