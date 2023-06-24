package io.edugma.domain.account.model

import kotlinx.serialization.Serializable
@Serializable
data class Token(val token: String) {
    fun getBearer() = "Bearer $token"
}
