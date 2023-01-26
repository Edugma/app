package io.edugma.domain.account.repository

import io.edugma.domain.account.model.Personal
import kotlinx.coroutines.flow.Flow

interface PersonalRepository {
    fun getPersonalInfo(): Flow<Result<Personal>>
    suspend fun setLocalPersonalInfo(personal: Personal)
    suspend fun getLocalPersonalInfo(): Personal?
}
