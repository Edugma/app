package com.edugma.core.api.repository

interface AuthInterceptorRepository {
    /**
     * Can start with "Bearer ".
     */
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getAccountId(): String?
}
