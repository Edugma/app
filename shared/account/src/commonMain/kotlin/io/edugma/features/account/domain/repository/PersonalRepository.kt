package io.edugma.features.account.domain.repository

import io.edugma.features.account.domain.model.Personal
import kotlinx.coroutines.flow.Flow

interface PersonalRepository {
    fun getPersonalInfo(): Flow<Result<Personal>>
    suspend fun getPersonalInfoSuspend(): Result<Personal>
    suspend fun setLocalPersonalInfo(personal: Personal)
    suspend fun getLocalPersonalInfo(): Personal?
}
