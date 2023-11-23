package io.edugma.features.account.domain.repository

import io.edugma.features.account.domain.model.Personal

interface PersonalRepository {
    suspend fun getPersonalInfoSuspend(): Result<Personal>
    suspend fun setLocalPersonalInfo(personal: Personal)
    suspend fun getLocalPersonalInfo(): Personal?
}
