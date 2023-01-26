package io.edugma.domain.account.repository

import kotlinx.coroutines.flow.Flow

interface AuthorizationRepository {
    fun authorization(login: String, password: String): Flow<Result<String>>
    suspend fun logout()
    suspend fun getSavedToken(): String?
}
