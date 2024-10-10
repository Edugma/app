package com.edugma.features.account.domain.repository

import com.edugma.features.account.domain.model.auth.Token

interface AuthorizationRepository {
    suspend fun authorize(login: String, password: String): Token

    suspend fun setCurrentToken(
        accessToken: String,
        refreshToken: String?,
    )

    suspend fun logout()

    suspend fun clearAccountCache()

    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?

    suspend fun getLkToken(): Result<String>
}
