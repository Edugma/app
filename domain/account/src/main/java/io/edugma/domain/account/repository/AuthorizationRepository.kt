package io.edugma.domain.account.repository

import io.edugma.domain.account.model.Login
import io.edugma.domain.account.model.Token
import kotlinx.coroutines.flow.Flow

interface AuthorizationRepository {
    fun authorization(login: String, password: String): Flow<Result<String>>
    suspend fun getSavedToken(): String?
}