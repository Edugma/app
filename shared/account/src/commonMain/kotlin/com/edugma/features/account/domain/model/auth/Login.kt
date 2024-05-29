package com.edugma.features.account.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val login: String,
    val password: String,
)
