package com.edugma.features.account.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthorizationRepository {
    fun authorization(login: String, password: String): Flow<Result<String>>

    suspend fun authorizationSuspend(login: String, password: String): Result<String>

    suspend fun logout()

    suspend fun getSavedToken(): String?

    suspend fun getLkToken(): Result<String>
}
