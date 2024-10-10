package com.edugma.features.account.domain.repository

interface AuthorizationRepository {
    suspend fun authorizationSuspend(login: String, password: String)

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
