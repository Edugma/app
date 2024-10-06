package com.edugma.features.account.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val token: String,
    val refreshToken: String? = null,
) {
    fun getBearer() = "Bearer $token"
}
